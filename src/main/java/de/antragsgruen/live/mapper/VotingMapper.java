package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.MQLocalizedText;
import de.antragsgruen.live.rabbitmq.dto.MQVotingEvent;
import org.springframework.lang.Nullable;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Turns one published voting event into the message a single subscriber receives: the sections they
 * may see, merged into one object, with every string in the language they are reading in.
 */
public final class VotingMapper {
    private VotingMapper() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param isAdmin whether this subscriber asked for the administration's view of the voting -
     *                which is what an /admin/ destination is, and which they were only allowed to
     *                subscribe to holding ROLE_VOTING_ADMIN
     * @param userId the subscriber's JWT subject, used to look up their own state
     * @param language the language the subscriber is reading Antragsgrün in
     * @param defaultLanguage the language to fall back to, as stated by the message
     */
    public static ObjectNode convertForSubscriber(
            MQVotingEvent event,
            boolean isAdmin,
            String userId,
            @Nullable String language,
            @Nullable String defaultLanguage
    ) {
        ObjectNode message = ((ObjectNode) event.everyone()).deepCopy();

        // A tally carries the counting alone, so it says by itself which voting it is about and when
        // it was taken; a full event says the same thing in its own fields, with the same values.
        message.put("id", event.blockId());
        message.put("current_time", event.currentTime());
        if (MQVotingEvent.KIND_TALLY.equals(event.kind())) {
            // Everything this event does not mention stays as the client has it - their own state,
            // above all, which nobody else's vote changes
            message.put("partial", true);
        }

        if (isAdmin && event.adminOnly() != null && event.adminOnly().isObject()) {
            message.setAll((ObjectNode) event.adminOnly());
        }

        if (event.defaultUserState() != null) {
            JsonNode ownState = (event.perUser() != null ? event.perUser().get(userId) : null);
            message.set("me", ownState != null ? ownState : event.defaultUserState());
        }

        return (ObjectNode) resolveLanguages(message, Set.copyOf(event.languages()), language, defaultLanguage);
    }

    /**
     * Replaces every localized string within the payload by its wording in the subscriber's language.
     * <p>
     * Which strings those are is not known here - the sections are opaque on purpose - so they are
     * recognized by their shape: an object whose members are all strings named after a language the
     * event was rendered in. That set is stated by the event itself, which is what keeps the rule
     * from guessing: no other object of a voting payload is keyed by language.
     */
    private static JsonNode resolveLanguages(JsonNode node, Set<String> languages, @Nullable String language, @Nullable String defaultLanguage) {
        if (node.isObject()) {
            MQLocalizedText localized = VotingMapper.asLocalizedText(node, languages);
            if (localized != null) {
                String resolved = localized.resolve(language, defaultLanguage);
                return resolved != null ? JsonNodeFactory.instance.textNode(resolved) : JsonNodeFactory.instance.nullNode();
            }

            ObjectNode converted = JsonNodeFactory.instance.objectNode();
            for (Map.Entry<String, JsonNode> property : node.properties()) {
                converted.set(property.getKey(), VotingMapper.resolveLanguages(property.getValue(), languages, language, defaultLanguage));
            }
            return converted;
        }

        if (node.isArray()) {
            ArrayNode converted = JsonNodeFactory.instance.arrayNode();
            for (JsonNode element : node) {
                converted.add(VotingMapper.resolveLanguages(element, languages, language, defaultLanguage));
            }
            return converted;
        }

        return node;
    }

    private static @Nullable MQLocalizedText asLocalizedText(JsonNode node, Set<String> languages) {
        if (node.isEmpty()) {
            return null;
        }

        Map<String, String> byLanguage = new LinkedHashMap<>();
        for (Map.Entry<String, JsonNode> property : node.properties()) {
            if (!languages.contains(property.getKey())) {
                return null;
            }
            if (!property.getValue().isTextual() && !property.getValue().isNull()) {
                return null;
            }
            byLanguage.put(property.getKey(), property.getValue().isNull() ? null : property.getValue().stringValue());
        }

        return MQLocalizedText.ofLanguages(byLanguage);
    }
}
