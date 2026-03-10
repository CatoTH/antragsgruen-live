package de.antragsgruen.live.websocket.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechSubqueueAdmin(
        Integer id,
        String name,
        WSSpeechSubqueueAdminItem[] onlist,
        WSSpeechSubqueueAdminItem[] applied
) {
}
