package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQSpeechQueue(
        int id,
        boolean isActive,
        MQSpeechQueueSettings settings,
        MQSpeechSubqueue[] subqueues,
        MQSpeechQueueActiveSlot[] slots,
        boolean requiresLogin,
        String otherActiveName,
        BigInteger currentTime
) {
    @JsonCreator
    public MQSpeechQueue {
    }
}
