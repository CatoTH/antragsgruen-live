package de.antragsgruen.live.rabbitmq.dto;

public class MQSpeechSubqueue {
    private int id;
    private String name;
    private MQSpeechSubqueueItem[] items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MQSpeechSubqueueItem[] getItems() {
        return items;
    }

    public void setItems(MQSpeechSubqueueItem[] items) {
        this.items = items;
    }
}
