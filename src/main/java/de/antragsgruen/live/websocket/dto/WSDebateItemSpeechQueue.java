package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSDebateItemSpeechQueue(
        @Getter Integer id,
        @Getter boolean isActive,
        @Getter String title
) {
    @Override
    @JsonProperty("is_active")
    public boolean isActive() {
        return isActive;
    }
}
