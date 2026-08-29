package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueueActiveSlot;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueueItem;
import de.antragsgruen.live.websocket.dto.WSSpeechActiveSlot;
import de.antragsgruen.live.websocket.dto.WSSpeechQueueUser;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueueUser;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueueUserItem;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public final class SpeechUserMapper {
    private SpeechUserMapper() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param language the language the receiving user is reading Antragsgrün in
     * @param defaultLanguage the language to fall back to, as stated by the message
     */
    public static WSSpeechQueueUser convertQueue(MQSpeechQueue queue, String userId, @Nullable String language, @Nullable String defaultLanguage) {
        WSSpeechSubqueueUser[] wsSubqueues = Stream
                .of(queue.subqueues())
                .map(subqueue -> convertSubqueue(subqueue, userId, queue.settings().showNames(), language, defaultLanguage))
                .toArray(WSSpeechSubqueueUser[]::new);

        WSSpeechActiveSlot[] wsActiveSlots = Stream
                .of(queue.slots())
                .filter(slot -> slot.dateStarted() != null)
                .map(slot -> convertActiveSlot(slot, language, defaultLanguage))
                .toArray(WSSpeechActiveSlot[]::new);

        boolean haveApplied = Stream.of(wsSubqueues).anyMatch(WSSpeechSubqueueUser::haveApplied);

        return new WSSpeechQueueUser(
                queue.id(),
                queue.isActive(),
                queue.settings().isOpen(),
                haveApplied,
                queue.settings().allowCustomNames(),
                queue.settings().isOpenPoo(),
                wsSubqueues,
                wsActiveSlots,
                queue.requiresLogin(),
                queue.currentTime(),
                queue.settings().speakingTime()
        );
    }

    private static WSSpeechSubqueueUser convertSubqueue(
            MQSpeechSubqueue subqueue,
            String userId,
            boolean showNames,
            @Nullable String language,
            @Nullable String defaultLanguage
    ) {
        boolean haveApplied = false;
        int numApplied = 0;

        for (MQSpeechSubqueueItem item : subqueue.items()) {
            Integer position = Optional.ofNullable(item.position()).orElse(0);
            if (position < 0) {
                numApplied++;
                if (UserIdMapper.isLoggedInUser(userId, item.userId()) || UserIdMapper.isAnonymousUser(userId, item.userToken())) {
                    haveApplied = true;
                }
            }
        }

        WSSpeechSubqueueUserItem[] items;
        if (showNames) {
            items = Stream
                    .of(subqueue.items())
                    .filter(item -> item.dateStarted() == null)
                    .map(SpeechUserMapper::convertSubqueueItem)
                    .toArray(WSSpeechSubqueueUserItem[]::new);
        } else {
            items = null;
        }

        return new WSSpeechSubqueueUser(
                subqueue.id(),
                subqueue.name().resolve(language, defaultLanguage),
                numApplied,
                haveApplied,
                items
        );
    }

    private static WSSpeechSubqueueUserItem convertSubqueueItem(MQSpeechSubqueueItem item) {
        return new WSSpeechSubqueueUserItem(
                item.id(),
                item.name(),
                item.isPointOfOrder(),
                item.dateApplied()
        );
    }

    private static WSSpeechActiveSlot convertActiveSlot(
            MQSpeechQueueActiveSlot activeSlot,
            @Nullable String language,
            @Nullable String defaultLanguage
    ) {
        return new WSSpeechActiveSlot(
                activeSlot.id(),
                activeSlot.subqueueId(),
                activeSlot.subqueueName().resolve(language, defaultLanguage),
                activeSlot.name(),
                activeSlot.position(),
                activeSlot.dateStarted(),
                activeSlot.dateStopped(),
                activeSlot.dateApplied()
        );
    }
}
