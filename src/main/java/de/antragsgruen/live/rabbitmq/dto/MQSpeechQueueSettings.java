package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

public record MQSpeechQueueSettings(
        boolean isOpen,
        boolean isOpenPoo,
        boolean allowCustomNames,
        boolean preferNonspeaker,
        boolean showNames,
        @Nullable Integer speakingTime
) {
    @JsonCreator
    public MQSpeechQueueSettings(
            @JsonProperty("isOpen") boolean isOpen,
            @JsonProperty("isOpenPoo") boolean isOpenPoo,
            @JsonProperty("allowCustomNames") boolean allowCustomNames,
            @JsonProperty("preferNonspeaker") boolean preferNonspeaker,
            @JsonProperty("showNames") boolean showNames,
            @JsonProperty("speakingTime") @Nullable Integer speakingTime
    ) {
        this.isOpen = isOpen;
        this.isOpenPoo = isOpenPoo;
        this.allowCustomNames = allowCustomNames;
        this.preferNonspeaker = preferNonspeaker;
        this.showNames = showNames;
        this.speakingTime = speakingTime;
    }
}
