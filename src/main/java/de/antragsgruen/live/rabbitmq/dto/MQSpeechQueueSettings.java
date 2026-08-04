package de.antragsgruen.live.rabbitmq.dto;

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
    @JsonCreator
    public MQSpeechQueueSettings {
    }
}
