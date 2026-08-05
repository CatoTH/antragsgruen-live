package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    /*
     * Hint: @JsonAlias for Backwards Compatibility with Antragsgrün <= 4.16
     */
    @JsonCreator
    public MQSpeechQueue(
            int id,
            @JsonAlias("isActive") boolean isActive,
            MQSpeechQueueSettings settings,
            MQSpeechSubqueue[] subqueues,
            MQSpeechQueueActiveSlot[] slots,
            @JsonAlias("requiresLogin") boolean requiresLogin,
            @JsonAlias("otherActiveName") String otherActiveName,
            @JsonAlias("currentTime") BigInteger currentTime
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
