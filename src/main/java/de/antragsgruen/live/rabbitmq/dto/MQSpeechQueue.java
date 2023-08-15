package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

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
    public MQSpeechQueue(
            int id,
            @JsonProperty("isActive") boolean isActive,
            MQSpeechQueueSettings settings,
            MQSpeechSubqueue[] subqueues,
            MQSpeechQueueActiveSlot[] slots,
            @JsonProperty("requiresLogin") boolean requiresLogin,
            String otherActiveName,
            BigInteger currentTime
    ) {
        this.id = id;
        this.isActive = isActive;
        this.settings = settings;
        this.subqueues = subqueues;
        this.slots = slots;
        this.requiresLogin = requiresLogin;
        this.otherActiveName = otherActiveName;
        this.currentTime = currentTime;
    }
}
