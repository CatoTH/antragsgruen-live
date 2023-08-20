package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechSubqueueUser(
        Integer id,
        String name,
        Integer numApplied,
        boolean haveApplied,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable WSSpeechSubqueueUserItem[] applied
) {
}
