package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public record MQUserEvent(
        String username
) {
    @JsonCreator
    public MQUserEvent {
    }
}
