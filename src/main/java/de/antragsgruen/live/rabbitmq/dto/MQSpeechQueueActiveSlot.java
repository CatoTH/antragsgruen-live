package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;

import java.util.Date;

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
