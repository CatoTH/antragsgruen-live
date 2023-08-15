package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WSSpeechSubqueueItem {
    private final Integer id;
    private final String name;
    private final boolean isPointOfOrder;
    private final Date appliedAt;

    public WSSpeechSubqueueItem(Integer id, String name, boolean isPointOfOrder, Date appliedAt) {
        this.id = id;
        this.name = name;
        this.isPointOfOrder = isPointOfOrder;
        this.appliedAt = appliedAt;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("is_point_of_order")
    public boolean isPointOfOrder() {
        return isPointOfOrder;
    }

    public Date getAppliedAt() {
        return appliedAt;
    }
}
