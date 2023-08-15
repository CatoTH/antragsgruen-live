package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WSSpeechSubqueue {
    private final Integer id;
    private final String name;
    private final Integer numApplied;
    private final boolean haveApplied;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final @Nullable WSSpeechSubqueueItem[] applied;

    public WSSpeechSubqueue(Integer id, String name, Integer numApplied, boolean haveApplied, @Nullable WSSpeechSubqueueItem[] applied) {
        this.id = id;
        this.name = name;
        this.numApplied = numApplied;
        this.haveApplied = haveApplied;
        this.applied = applied;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNumApplied() {
        return numApplied;
    }

    public boolean isHaveApplied() {
        return haveApplied;
    }

    public @Nullable WSSpeechSubqueueItem[] getApplied() {
        return applied;
    }
}
