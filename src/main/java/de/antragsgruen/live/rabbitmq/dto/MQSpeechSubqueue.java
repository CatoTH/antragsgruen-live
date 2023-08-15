package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public record MQSpeechSubqueue(int id, String name, MQSpeechSubqueueItem[] items) {
    @JsonCreator
    public MQSpeechSubqueue {
    }
}
