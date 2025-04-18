package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSAgendaItem(
        int id,
        String type,
        @Nullable String code,
        String title,
        @Nullable String time,
        @Nullable String date,
        WSAgendaItemSettings settings,
        WSAgendaItem[] children
) {
}
