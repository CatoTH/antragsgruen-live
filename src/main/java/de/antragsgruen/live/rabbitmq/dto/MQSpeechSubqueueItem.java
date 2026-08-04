package de.antragsgruen.live.rabbitmq.dto;

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
    @JsonCreator
    public MQSpeechSubqueueItem {
    }
}
