package de.antragsgruen.live.websocket.dto;

public class WSGreeting {
    private String content;

    public WSGreeting() {}

    public WSGreeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
