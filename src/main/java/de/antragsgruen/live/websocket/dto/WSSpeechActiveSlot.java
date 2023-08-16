package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.HashMap;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WSSpeechActiveSlot {
    private final Integer id;
    private final HashMap<String, Object> subqueue;
    private final String name;
    private final @Nullable Integer position;
    private final @Nullable Date dateStarted;
    private final @Nullable Date dateStopped;
    private final @Nullable Date dateApplied;

    public WSSpeechActiveSlot(Integer id, @Nullable Integer subqueueId, String subqueueName, String name, @Nullable Integer position, @Nullable Date dateStarted, @Nullable Date dateStopped, @Nullable Date dateApplied) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.dateStarted = dateStarted;
        this.dateStopped = dateStopped;
        this.dateApplied = dateApplied;

        this.subqueue = new HashMap<>(2);
        this.subqueue.put("id", subqueueId);
        this.subqueue.put("name", subqueueName);
    }
}
