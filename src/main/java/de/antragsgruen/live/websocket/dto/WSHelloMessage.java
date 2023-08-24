package de.antragsgruen.live.websocket.dto;

import org.springframework.lang.Nullable;

public class WSHelloMessage {
    private @Nullable String name;

    public WSHelloMessage() {
    }

    public WSHelloMessage(@Nullable String name) {
        this.name = name;
    }

    public @Nullable String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
