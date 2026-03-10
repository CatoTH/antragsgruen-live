package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
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
