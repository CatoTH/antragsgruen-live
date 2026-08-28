package de.antragsgruen.live.rabbitmq.dto;

import org.springframework.lang.Nullable;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A string of an event that depends on the language the reader is browsing Antragsgrün in - either
 * because it is translated, or because the content itself exists in several languages.
 * <p>
 * An event is published once for the whole consultation, but delivered to users who may be reading
 * it in different languages. Antragsgrün therefore sends every language of the consultation and this
 * server picks the one matching each subscriber - the language being the last part of the
 * destination they subscribed to - falling back to the message's default language.
 * <p>
 * Antragsgrün <= 4.17 sends a plain string instead of an object, which is why both shapes are
 * accepted here.
 */
@JsonDeserialize(using = MQLocalizedText.Deserializer.class)
public record MQLocalizedText(
        @Nullable String plain,
        Map<String, String> byLanguage
) {
    public static MQLocalizedText ofPlainString(@Nullable String plain) {
        return new MQLocalizedText(plain, Map.of());
    }

    public static MQLocalizedText ofLanguages(Map<String, String> byLanguage) {
        return new MQLocalizedText(null, byLanguage);
    }

    /**
     * @param language the language of the subscriber this event is being delivered to
     * @param defaultLanguage the language to use if the message does not contain the subscriber's
     */
    public @Nullable String resolve(@Nullable String language, @Nullable String defaultLanguage) {
        if (this.byLanguage.isEmpty()) {
            return this.plain;
        }

        if (language != null && this.byLanguage.containsKey(language)) {
            return this.byLanguage.get(language);
        }
        if (defaultLanguage != null && this.byLanguage.containsKey(defaultLanguage)) {
            return this.byLanguage.get(defaultLanguage);
        }

        return this.byLanguage.values().iterator().next();
    }

    static final class Deserializer extends ValueDeserializer<MQLocalizedText> {
        private static final TypeReference<LinkedHashMap<String, String>> MAP_TYPE = new TypeReference<>() {
        };

        @Override
        public MQLocalizedText deserialize(JsonParser p, DeserializationContext ctxt) {
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                return MQLocalizedText.ofPlainString(p.getString());
            }
            if (p.currentToken() == JsonToken.VALUE_NULL) {
                return MQLocalizedText.ofPlainString(null);
            }

            return MQLocalizedText.ofLanguages(ctxt.readValue(p, MAP_TYPE));
        }

        @Override
        public MQLocalizedText getNullValue(DeserializationContext ctxt) {
            return MQLocalizedText.ofPlainString(null);
        }
    }
}
