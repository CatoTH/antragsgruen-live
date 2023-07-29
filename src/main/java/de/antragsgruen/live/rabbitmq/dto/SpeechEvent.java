package de.antragsgruen.live.rabbitmq.dto;

public class SpeechEvent {
    public String queueName;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
