package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;

public record MQDebateState(
        @Nullable MQDebateItem current
) {
    @JsonCreator
    public MQDebateState(
            @Nullable MQDebateItem current
    ) {
        this.current = current;
    }
}
