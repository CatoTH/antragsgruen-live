package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueueActiveSlot;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueueItem;
import de.antragsgruen.live.websocket.dto.WSSpeechActiveSlot;
import de.antragsgruen.live.websocket.dto.WSSpeechQueueUser;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueueUser;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueueUserItem;

import java.util.Optional;
import java.util.stream.Stream;

public final class SpeechUserMapper {
    private SpeechUserMapper() {
        throw new UnsupportedOperationException();
    }

    public static WSSpeechQueueUser convertQueue(MQSpeechQueue queue, String userId) {
        WSSpeechSubqueueUser[] wsSubqueues = Stream
                .of(queue.subqueues())
                .map(subqueue -> convertSubqueue(subqueue, userId, queue.settings().showNames()))
                .toArray(WSSpeechSubqueueUser[]::new);

        WSSpeechActiveSlot[] wsActiveSlots = Stream
                .of(queue.slots())
                .filter(slot -> slot.dateStarted() != null)
                .map(SpeechUserMapper::convertActiveSlot)
                .toArray(WSSpeechActiveSlot[]::new);

        boolean haveApplied = Stream.of(wsSubqueues).anyMatch(WSSpeechSubqueueUser::haveApplied);

        return new WSSpeechQueueUser(
                queue.id(),
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

    private static WSSpeechSubqueueUser convertSubqueue(MQSpeechSubqueue subqueue, String userId, boolean showNames) {
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
                subqueue.name(),
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

    private static WSSpeechActiveSlot convertActiveSlot(MQSpeechQueueActiveSlot activeSlot) {
        return new WSSpeechActiveSlot(
                activeSlot.id(),
                activeSlot.subqueueId(),
                activeSlot.subqueueName(),
                activeSlot.name(),
                activeSlot.position(),
                activeSlot.dateStarted(),
                activeSlot.dateStopped(),
                activeSlot.dateApplied()
        );
    }
}
