package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechSubqueue(
        Integer id,
        String name,
        Integer numApplied,
        boolean haveApplied,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable WSSpeechSubqueueItem[] applied
) {
}
