package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.MQAgendaItem;
import de.antragsgruen.live.websocket.dto.WSAgendaItem;

import java.util.stream.Stream;

public final class AgendaItemMapper {
    private AgendaItemMapper() {
        throw new UnsupportedOperationException();
    }

    public static WSAgendaItem[] convertAgendaItems(MQAgendaItem[] items) {
        return Stream
                .of(items)
                .map(AgendaItemMapper::convertAgendaItem)
                .toArray(WSAgendaItem[]::new);
    }

    private static WSAgendaItem convertAgendaItem(MQAgendaItem item) {

    }
}
