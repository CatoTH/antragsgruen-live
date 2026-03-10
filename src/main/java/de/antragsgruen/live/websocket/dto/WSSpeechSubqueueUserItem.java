package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechSubqueueUserItem(
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
