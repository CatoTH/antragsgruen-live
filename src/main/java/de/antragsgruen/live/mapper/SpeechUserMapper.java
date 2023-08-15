package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueueActiveSlot;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueueItem;
import de.antragsgruen.live.websocket.dto.WSSpeechActiveSlot;
import de.antragsgruen.live.websocket.dto.WSSpeechQueue;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueue;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueueItem;

import java.util.stream.Stream;

public class SpeechUserMapper {
    public static WSSpeechQueue convertQueue(MQSpeechQueue queue, String userId) {
        WSSpeechSubqueue[] wsSubqueues = Stream
                .of(queue.subqueues())
                .map(subqueue -> convertSubqueue(subqueue, userId, queue.settings().showNames()))
                .toArray(WSSpeechSubqueue[]::new);

        WSSpeechActiveSlot[] wsActiveSlots = Stream
                .of(queue.slots())
                .filter(slot -> slot.dateStarted() != null)
                .map(SpeechUserMapper::convertActiveSlot)
                .toArray(WSSpeechActiveSlot[]::new);

        boolean haveApplied = Stream.of(wsSubqueues).anyMatch(WSSpeechSubqueue::isHaveApplied);

        return new WSSpeechQueue(
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

    private static WSSpeechSubqueue convertSubqueue(MQSpeechSubqueue subqueue, String userId, boolean showNames) {
        boolean haveApplied = false;
        int numApplied = 0;

        for (MQSpeechSubqueueItem item : subqueue.items()) {
            if (item.position() < 0) {
                numApplied++;
                if (UserIdMapper.isLoggedInUser(userId, item.userId()) || UserIdMapper.isAnonymousUser(userId, item.userToken())) {
                    haveApplied = true;
                }
            }
        }

        WSSpeechSubqueueItem[] items;
        if (showNames) {
            items = Stream
                    .of(subqueue.items())
                    .filter(item -> item.dateStarted() == null)
                    .map(SpeechUserMapper::convertSubqueueItem)
                    .toArray(WSSpeechSubqueueItem[]::new);
        } else {
            items = null;
        }

        return new WSSpeechSubqueue(
                subqueue.id(),
                subqueue.name(),
                numApplied,
                haveApplied,
                items
        );
    }

    private static WSSpeechSubqueueItem convertSubqueueItem(MQSpeechSubqueueItem item) {
        return new WSSpeechSubqueueItem(
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
