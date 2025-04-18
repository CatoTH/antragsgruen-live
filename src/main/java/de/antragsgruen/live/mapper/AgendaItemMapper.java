package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.MQAgendaItem;
import de.antragsgruen.live.websocket.dto.WSAgendaItem;

public final class AgendaItemMapper {
    private AgendaItemMapper() {
        throw new UnsupportedOperationException();
    }

    public static WSSAgendaItem[] convertAgendaItems(MQAgendaItem[] items) {
        // @TOOD
    }
}
