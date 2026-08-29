package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQSpeechSubqueue(
        Integer id,
        MQLocalizedText name,
        MQSpeechSubqueueItem[] items
) {
    @JsonCreator
    public MQSpeechSubqueue {
    }
}
