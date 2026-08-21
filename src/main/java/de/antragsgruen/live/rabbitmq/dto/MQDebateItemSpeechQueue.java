package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQDebateItemSpeechQueue(
        Integer id,
        boolean isActive,
        String title
) {
    @JsonCreator
    public MQDebateItemSpeechQueue {
    }
}
