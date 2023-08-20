package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechSubqueueAdminItem(
        @Getter Integer id,
        @Getter String name,
        @Getter @Nullable Integer userId,
        @Getter @Nullable String userToken,
        @Getter boolean isPointOfOrder,
        @Getter Date appliedAt
) {
    @Override
    @JsonProperty("is_point_of_order")
    public boolean isPointOfOrder() {
        return isPointOfOrder;
    }
}
