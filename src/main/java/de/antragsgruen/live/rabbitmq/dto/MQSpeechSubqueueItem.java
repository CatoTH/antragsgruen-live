package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.util.Date;

public record MQSpeechSubqueueItem(
        Integer id,
        String name,
        @Nullable Integer userId,
        @Nullable String userToken,
        boolean isPointOfOrder,
        Date dateApplied,
        @Nullable Date dateStarted,
        @Nullable Integer position
) {
    @JsonCreator
    public MQSpeechSubqueueItem(
            Integer id,
            String name,
            @Nullable Integer userId,
            @Nullable String userToken,
            @JsonProperty("isPointOfOrder") boolean isPointOfOrder,
            Date dateApplied,
            @Nullable Date dateStarted,
            @Nullable Integer position
    ) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.userToken = userToken;
        this.isPointOfOrder = isPointOfOrder;
        this.dateApplied = dateApplied;
        this.dateStarted = dateStarted;
        this.position = position;
    }
}
