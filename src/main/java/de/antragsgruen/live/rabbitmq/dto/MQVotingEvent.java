package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * The state of one voting block, as published by Antragsgrün whenever it changes.
 * <p>
 * Unlike the speaking lists and the debate, the sections of this event are not mapped field by
 * field: this server only decides <em>which</em> sections a subscriber is given, never what is in
 * them. Whatever Antragsgrün considers a participant's business is in "everyone"; what only the
 * administration may see - the single votes of a non-secret voting above all - is in "adminOnly",
 * and is delivered only to subscribers of an /admin/ destination, which requires ROLE_VOTING_ADMIN.
 * A vote that nobody may see is in no section at all, as Antragsgrün never serializes it.
 * <p>
 * Keeping the sections opaque means the payload of a voting can grow or change in Antragsgrün
 * without this server having to be taught about it - and, more importantly, that no field can end
 * up in the wrong section here because a DTO fell out of step.
 *
 * @param kind "full" for the whole state, "tally" for an event that carries only the counting -
 *             which is what a cast vote triggers, and which clients merge into what they have
 * @param languages the languages the localized strings inside the sections were rendered in
 * @param perUser one entry per user Antragsgrün knows something about, keyed by the JWT subject;
 *                everybody else is described by defaultUserState. Only in "full" events.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQVotingEvent(
        String kind,
        int blockId,
        long stateVersion,
        long currentTime,
        List<String> languages,
        JsonNode everyone,
        @Nullable JsonNode adminOnly,
        @Nullable JsonNode defaultUserState,
        @Nullable JsonNode perUser
) {
    public static final String KIND_TALLY = "tally";

    @JsonCreator
    public MQVotingEvent {
    }
}
