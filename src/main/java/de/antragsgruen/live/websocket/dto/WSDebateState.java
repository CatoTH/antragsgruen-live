package de.antragsgruen.live.websocket.dto;

import lombok.Getter;
import org.springframework.lang.Nullable;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSDebateState(
        @Getter @Nullable WSDebateItem current
) {
}
