package de.antragsgruen.live.websocket.dto;

import lombok.Getter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSDebateItemVotingBlock(
        @Getter Integer id,
        @Getter Integer status,
        @Getter String title
) {
}
