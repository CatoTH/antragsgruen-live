package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQSpeechQueueSettings(
        boolean isOpen,
        boolean isOpenPoo,
        boolean allowCustomNames,
        boolean preferNonspeaker,
        boolean showNames,
        @Nullable Integer speakingTime
) {
    /*
     * Hint: @JsonAlias for Backwards Compatibility with Antragsgrün <= 4.16
     */
    @JsonCreator
    public MQSpeechQueueSettings(
            @JsonAlias("isOpen") boolean isOpen,
            @JsonAlias("isOpenPoo") boolean isOpenPoo,
            @JsonAlias("allowCustomNames") boolean allowCustomNames,
            @JsonAlias("preferNonspeaker") boolean preferNonspeaker,
            @JsonAlias("showNames") boolean showNames,
            @JsonAlias("speakingTime") @Nullable Integer speakingTime
    ) {
        this.isOpen = isOpen;
        this.isOpenPoo = isOpenPoo;
        this.allowCustomNames = allowCustomNames;
        this.preferNonspeaker = preferNonspeaker;
        this.showNames = showNames;
        this.speakingTime = speakingTime;
    }
}
