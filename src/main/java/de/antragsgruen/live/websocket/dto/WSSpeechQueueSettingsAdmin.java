package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechQueueSettingsAdmin(
        boolean isOpen,
        boolean isOpenPoo,
        boolean allowCustomNames,
        boolean preferNonspeaker,
        boolean showNames,
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
