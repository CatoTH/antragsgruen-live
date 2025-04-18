package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSAgendaItem(
        int id,
        String type,
        @Nullable String code,
        String title,
        @Nullable String time,
        @Nullable String date,
        MQAgendaItemSettings settings,
        MQAgendaItem[] children
) {
}
