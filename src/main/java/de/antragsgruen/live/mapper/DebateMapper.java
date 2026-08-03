package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.*;
import de.antragsgruen.live.websocket.dto.WSDebateItem;
import de.antragsgruen.live.websocket.dto.WSDebateState;

public final class DebateMapper {
    private DebateMapper() {
        throw new UnsupportedOperationException();
    }

    public static WSDebateState convertState(MQDebateState mqState, String userId) {
        if (mqState.current() == null) {
            return new WSDebateState(null);
        } else {
            return new WSDebateState(DebateMapper.convertItem(mqState.current()));
        }
    }

    public static WSDebateItem convertItem(MQDebateItem mqItem) {
        return new WSDebateItem(
                mqItem.id(),
                mqItem.targetType(),
                mqItem.targetId(),
                mqItem.title(),
                mqItem.dateStarted(),
                mqItem.titleWithPrefix(),
                mqItem.initiatorsHtml(),
                mqItem.urlJson(),
                mqItem.urlHtml(),
                mqItem.speechQueueId(),
                mqItem.votingBlockId()
        );
    }
}
