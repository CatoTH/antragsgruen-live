package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public record MQAgendaItemSettings(
        boolean hasSpeakingList,
        boolean inProposedProcedures,
        int[] motionTypes
) {
    @JsonCreator
    public MQAgendaItemSettings {
    }
}
