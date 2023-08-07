package de.antragsgruen.live.websocket.dto;

public class WSSpeechQueue {
    public String userId;

    public WSSpeechQueue(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
