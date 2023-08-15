package de.antragsgruen.live.websocket.dto;

import org.springframework.lang.Nullable;

public class WSGreeting {
    private @Nullable String content;

    public WSGreeting() {}

    public WSGreeting(@Nullable String content) {
        this.content = content;
    }

    public @Nullable String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }
}
