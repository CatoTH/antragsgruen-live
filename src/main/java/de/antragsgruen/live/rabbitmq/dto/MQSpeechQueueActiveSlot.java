package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQSpeechQueueActiveSlot(
        Integer id,
        @Nullable Integer subqueueId,
        String subqueueName,
        String name,
        @Nullable Integer userId,
        @Nullable String userToken,
        Integer position,
        @Nullable Date dateStarted,
        @Nullable Date dateStopped,
        @Nullable Date dateApplied
) {
    /*
     * Hint: @JsonAlias for Backwards Compatibility with Antragsgrün <= 4.16
     */
    @JsonCreator
    public MQSpeechQueueActiveSlot(
            Integer id,
            @JsonAlias("subqueueId") @Nullable Integer subqueueId,
            @JsonAlias("subqueueName") String subqueueName,
            String name,
            @JsonAlias("userId") @Nullable Integer userId,
            @JsonAlias("userToken") @Nullable String userToken,
            Integer position,
            @JsonAlias("dateStarted") @Nullable Date dateStarted,
            @JsonAlias("dateStopped") @Nullable Date dateStopped,
            @JsonAlias("dateApplied") @Nullable Date dateApplied
    ) {
        this.id = id;
        this.subqueueId = subqueueId;
        this.subqueueName = subqueueName;
        this.name = name;
        this.userId = userId;
        this.userToken = userToken;
        this.position = position;
        this.dateStarted = dateStarted;
        this.dateStopped = dateStopped;
        this.dateApplied = dateApplied;
    }
}
