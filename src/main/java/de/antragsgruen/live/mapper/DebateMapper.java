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

    /**
     * @param language the language the receiving user is reading Antragsgrün in
     * @param defaultLanguage the language to fall back to, as stated by the message
     */
    public static WSDebateState convertState(MQDebateState mqState, @Nullable String language, @Nullable String defaultLanguage) {
        if (mqState.current() == null) {
            return new WSDebateState(null);
        } else {
            return new WSDebateState(DebateMapper.convertItem(mqState.current(), language, defaultLanguage));
        }
    }

    public static WSDebateItem convertItem(MQDebateItem mqItem, @Nullable String language, @Nullable String defaultLanguage) {
        return new WSDebateItem(
                mqItem.id(),
                mqItem.targetType(),
                mqItem.targetId(),
                mqItem.title().resolve(language, defaultLanguage),
                mqItem.dateStarted(),
                DebateMapper.resolve(mqItem.titleWithPrefix(), language, defaultLanguage),
                DebateMapper.resolve(mqItem.initiatorsHtml(), language, defaultLanguage),
                mqItem.urlJson(),
                mqItem.urlHtml(),
                DebateMapper.convertSpeechQueue(mqItem.speechQueue(), language, defaultLanguage),
                DebateMapper.convertVotingBlock(mqItem.votingBlock())
        );
    }

    private static @Nullable String resolve(@Nullable MQLocalizedText text, @Nullable String language, @Nullable String defaultLanguage) {
        return (text != null ? text.resolve(language, defaultLanguage) : null);
    }

    private static @Nullable WSDebateItemSpeechQueue convertSpeechQueue(
            @Nullable MQDebateItemSpeechQueue mqQueue,
            @Nullable String language,
            @Nullable String defaultLanguage
    ) {
        if (mqQueue == null) {
            return null;
        }

        String title = mqQueue.title().resolve(language, defaultLanguage);

        return new WSDebateItemSpeechQueue(mqQueue.id(), mqQueue.isActive(), title);
    }

    private static @Nullable WSDebateItemVotingBlock convertVotingBlock(@Nullable MQDebateItemVotingBlock mqBlock) {
        if (mqBlock == null) {
            return null;
        }

        return new WSDebateItemVotingBlock(mqBlock.id(), mqBlock.status(), mqBlock.title());
    }
}
