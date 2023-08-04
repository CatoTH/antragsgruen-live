package de.antragsgruen.live.rabbitmq.dto;

public class SpeechSubqueue {
    private int id;
    private String name;
    private SpeechSubqueueItem[] items;

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

    public SpeechSubqueueItem[] getItems() {
        return items;
    }

    public void setItems(SpeechSubqueueItem[] items) {
        this.items = items;
    }
}
