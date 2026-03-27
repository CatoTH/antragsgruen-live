package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import org.springframework.lang.Nullable;

public record MQAgendaItem(
        int id,
        String type,
        @Nullable String code,
        String title,
        @Nullable String time,
        @Nullable String date,
        MQAgendaItemSettings settings,
        MQAgendaItem[] children
) {
    @JsonCreator
    public MQAgendaItem {
    }
}
