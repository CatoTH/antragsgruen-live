package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechQueueUser(
        Integer id,
        boolean isOpen,
        boolean haveApplied,
        boolean allowCustomNames,
        boolean isOpenPoo,
        WSSpeechSubqueueUser[] subqueues,
        WSSpeechActiveSlot[] slots,
        boolean requiresLogin,
        BigInteger currentTime,
        @Nullable Integer speakingTime
) {
    @Override
    @JsonProperty("is_open")
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    @JsonProperty("is_open_poo")
    public boolean isOpenPoo() {
        return isOpenPoo;
    }
}
