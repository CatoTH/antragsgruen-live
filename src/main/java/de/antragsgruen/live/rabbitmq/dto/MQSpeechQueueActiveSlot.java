package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQSpeechQueueActiveSlot(
        Integer id,
        @Nullable Integer subqueueId,
        String subqueueName,
        String name,
        @Nullable Integer userId,
        @Nullable String userToken,
        Integer position,
        @Nullable Date dateStarted,
        @Nullable Date dateStopped,
        @Nullable Date dateApplied
) {
    @JsonCreator
    public MQSpeechQueueActiveSlot {
    }
}
