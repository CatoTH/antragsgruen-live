package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WSSpeechSubqueue {
    private int id;
    private String name;
    private int numApplied;
    private boolean haveApplied;
    private WSSpeechSubqueueItem[] applied;

    public WSSpeechSubqueue(int id) {
        this.id = id;
    }

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

    public int getNumApplied() {
        return numApplied;
    }

    public void setNumApplied(int numApplied) {
        this.numApplied = numApplied;
    }

    public boolean isHaveApplied() {
        return haveApplied;
    }

    public void setHaveApplied(boolean haveApplied) {
        this.haveApplied = haveApplied;
    }

    public WSSpeechSubqueueItem[] getApplied() {
        return applied;
    }

    public void setApplied(WSSpeechSubqueueItem[] applied) {
        this.applied = applied;
    }
}