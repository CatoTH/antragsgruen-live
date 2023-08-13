package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.HashMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WSSpeechActiveSlot {
    private int id;
    private HashMap<String, Object> subqueue;
    private String name;
    private @Nullable Integer position;
    private @Nullable Date dateStarted;
    private @Nullable Date dateStopped;
    private @Nullable Date dateApplied;

    public WSSpeechActiveSlot(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubqueue(@Nullable Integer id, String name) {
        this.subqueue = new HashMap<>(2);
        this.subqueue.put("id", id);
        this.subqueue.put("name", name);
    }

    public HashMap<String, Object> getSubqueue() {
        return subqueue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Nullable Integer getPosition() {
        return position;
    }

    public void setPosition(@Nullable Integer position) {
        this.position = position;
    }

    public @Nullable Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(@Nullable Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public @Nullable Date getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(@Nullable Date dateStopped) {
        this.dateStopped = dateStopped;
    }

    public @Nullable Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(@Nullable Date dateApplied) {
        this.dateApplied = dateApplied;
    }
}
