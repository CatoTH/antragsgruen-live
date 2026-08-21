package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.*;
import de.antragsgruen.live.websocket.dto.WSDebateItem;
import de.antragsgruen.live.websocket.dto.WSDebateItemSpeechQueue;
import de.antragsgruen.live.websocket.dto.WSDebateItemVotingBlock;
import de.antragsgruen.live.websocket.dto.WSDebateState;
import org.springframework.lang.Nullable;

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
                DebateMapper.convertSpeechQueue(mqItem.speechQueue()),
                DebateMapper.convertVotingBlock(mqItem.votingBlock())
        );
    }

    private static @Nullable WSDebateItemSpeechQueue convertSpeechQueue(@Nullable MQDebateItemSpeechQueue mqQueue) {
        if (mqQueue == null) {
            return null;
        }

        return new WSDebateItemSpeechQueue(mqQueue.id(), mqQueue.isActive(), mqQueue.title());
    }

    private static @Nullable WSDebateItemVotingBlock convertVotingBlock(@Nullable MQDebateItemVotingBlock mqBlock) {
        if (mqBlock == null) {
            return null;
        }

        return new WSDebateItemVotingBlock(mqBlock.id(), mqBlock.status(), mqBlock.title());
    }
}
