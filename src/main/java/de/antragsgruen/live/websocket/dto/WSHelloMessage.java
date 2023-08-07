package de.antragsgruen.live.websocket.dto;

public class WSHelloMessage {
    private String name;

    public WSHelloMessage() {}

    public WSHelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
