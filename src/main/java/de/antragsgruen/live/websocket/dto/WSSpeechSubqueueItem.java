package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechSubqueueItem(
        @Getter Integer id,
        @Getter String name,
        @Getter boolean isPointOfOrder,
        @Getter Date appliedAt
) {
    @Override
    @JsonProperty("is_point_of_order")
    public boolean isPointOfOrder() {
        return isPointOfOrder;
    }
}
