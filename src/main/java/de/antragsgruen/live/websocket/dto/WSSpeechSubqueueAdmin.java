package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSSpeechSubqueueAdmin(
        Integer id,
        String name,
        WSSpeechSubqueueAdminItem[] onlist,
        WSSpeechSubqueueAdminItem[] applied
) {
}
