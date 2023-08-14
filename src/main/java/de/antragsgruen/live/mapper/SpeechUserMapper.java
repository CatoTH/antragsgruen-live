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
                .of(queue.getSubqueues())
                .map(subqueue -> convertSubqueue(subqueue, userId, queue.getSettings().isShowNames()))
                .toArray(WSSpeechSubqueue[]::new);

        WSSpeechActiveSlot[] wsActiveSlots = Stream
                .of(queue.getSlots())
                .filter(slot -> slot.getDateStarted() != null)
                .map(SpeechUserMapper::convertActiveSlot)
                .toArray(WSSpeechActiveSlot[]::new);

        boolean haveApplied = Stream.of(wsSubqueues).anyMatch(WSSpeechSubqueue::isHaveApplied);

        WSSpeechQueue wsDto = new WSSpeechQueue(queue.getId());
        wsDto.setOpen(queue.getSettings().isOpen());
        wsDto.setHaveApplied(haveApplied);
        wsDto.setAllowCustomNames(queue.getSettings().isAllowCustomNames());
        wsDto.setOpenPoo(queue.getSettings().isOpenPoo());
        wsDto.setSubqueues(wsSubqueues);
        wsDto.setSlots(wsActiveSlots);
        wsDto.setRequiresLogin(queue.isRequiresLogin());
        wsDto.setCurrentTime(queue.getCurrentTime());
        wsDto.setSpeakingTime(queue.getSettings().getSpeakingTime());

        return wsDto;
    }

    private static WSSpeechSubqueue convertSubqueue(MQSpeechSubqueue subqueue, String userId, boolean showNames) {
        boolean haveApplied = false;
        int numApplied = 0;

        for (MQSpeechSubqueueItem item : subqueue.getItems()) {
            if (item.getPosition() < 0) {
                numApplied++;
                if (UserIdMapper.isLoggedInUser(userId, item.getUserId()) || UserIdMapper.isAnonymousUser(userId, item.getUserToken())) {
                    haveApplied = true;
                }
            }
        }

        WSSpeechSubqueue wsSubqueue = new WSSpeechSubqueue(subqueue.getId());
        wsSubqueue.setName(subqueue.getName());
        wsSubqueue.setHaveApplied(haveApplied);
        wsSubqueue.setNumApplied(numApplied);
        if (showNames) {
            WSSpeechSubqueueItem[] items = Stream
                    .of(subqueue.getItems())
                    .filter(item -> item.getDateStarted() == null)
                    .map(SpeechUserMapper::convertSubqueueItem)
                    .toArray(WSSpeechSubqueueItem[]::new);
            wsSubqueue.setApplied(items);
        } else {
            wsSubqueue.setApplied(null);
        }

        return wsSubqueue;
    }

    private static WSSpeechSubqueueItem convertSubqueueItem(MQSpeechSubqueueItem item) {
        WSSpeechSubqueueItem wsItem = new WSSpeechSubqueueItem(item.getId());
        wsItem.setName(item.getName());
        wsItem.setPointOfOrder(item.isPointOfOrder());
        wsItem.setAppliedAt(item.getDateApplied());

        return wsItem;
    }

    private static WSSpeechActiveSlot convertActiveSlot(MQSpeechQueueActiveSlot activeSlot) {
        WSSpeechActiveSlot wsActiveSlot = new WSSpeechActiveSlot(activeSlot.getId());
        wsActiveSlot.setSubqueue(activeSlot.getSubqueueId(), activeSlot.getSubqueueName());
        wsActiveSlot.setName(activeSlot.getName());
        wsActiveSlot.setPosition(activeSlot.getPosition());
        wsActiveSlot.setDateApplied(activeSlot.getDateApplied());
        wsActiveSlot.setDateStarted(activeSlot.getDateStarted());
        wsActiveSlot.setDateStopped(activeSlot.getDateStopped());

        return wsActiveSlot;
    }
}
