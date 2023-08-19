package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public record MQSpeechSubqueue(Integer id, String name, MQSpeechSubqueueItem[] items) {
    @JsonCreator
    public MQSpeechSubqueue {
    }
}
