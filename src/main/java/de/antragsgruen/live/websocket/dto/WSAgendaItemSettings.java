package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSAgendaItemSettings(
        boolean hasSpeakingList,
        boolean inProposedProcedures,
        int[] motion_types
) {
}
