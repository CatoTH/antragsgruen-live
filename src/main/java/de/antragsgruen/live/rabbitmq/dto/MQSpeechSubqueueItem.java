package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
    /*
     * Hint: @JsonAlias for Backwards Compatibility with Antragsgrün <= 4.16
     */
    @JsonCreator
    public MQSpeechSubqueueItem(
            Integer id,
            String name,
            @JsonAlias("userId") @Nullable Integer userId,
            @JsonAlias("userToken") @Nullable String userToken,
            @JsonAlias("isPointOfOrder") boolean isPointOfOrder,
            @JsonAlias("dateApplied") Date dateApplied,
            @JsonAlias("dateStarted") @Nullable Date dateStarted,
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
