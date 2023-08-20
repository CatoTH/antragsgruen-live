package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechQueueAdmin(
        Integer id,
        boolean isActive,
        WSSpeechQueueSettingsAdmin settings,
        WSSpeechSubqueueAdmin[] subqueues,
        WSSpeechActiveSlot[] slots,
        @Nullable String otherActiveName,
        BigInteger currentTime
) {
    @Override
    @JsonProperty("is_active")
    public boolean isActive() {
        return isActive;
    }
}
