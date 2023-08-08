package de.antragsgruen.live;

import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueueActiveSlot;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueueItem;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSSpeechActiveSlot;
import de.antragsgruen.live.websocket.dto.WSSpeechQueue;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueue;
import de.antragsgruen.live.websocket.dto.WSSpeechSubqueueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class SpeechUserHandler {
    @Autowired
    private Sender sender;

    @Autowired
    private SimpUserRegistry userRegistry;

    private final Logger logger = LoggerFactory.getLogger(SpeechUserHandler.class);


    public void onSpeechEvent(String subdomain, String consultation, MQSpeechQueue mqQueue) {
        logger.info("Sending speech event to " + userRegistry.getUserCount() + " user(s)");

        for (SimpUser user : userRegistry.getUsers()) {
            WSSpeechQueue wsQueue = convertQueue(mqQueue, user.getName());
            sender.sendToUser(subdomain, consultation, user.getName(), Sender.USER_CHANNEL_SPEECH, wsQueue);
        }
    }

    private WSSpeechQueue convertQueue(MQSpeechQueue queue, String userId) {
        WSSpeechSubqueue[] wsSubqueues = Stream
                .of(queue.getSubqueues())
                .map(subqueue -> convertSubqueue(subqueue, userId, queue.getSettings().isShowNames()))
                .toArray(WSSpeechSubqueue[]::new);

        WSSpeechActiveSlot[] wsActiveSlots = Stream
                .of(queue.getSlots())
                .map(this::convertActiveSlot)
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

    private WSSpeechSubqueue convertSubqueue(MQSpeechSubqueue subqueue, String userId, boolean showNames) {
        boolean haveApplied = false;
        int numApplied = 0;

        for (MQSpeechSubqueueItem item : subqueue.getItems()) {
            if (item.getPosition() < 0) {
                numApplied++;
                if (item.getUserId() != null && item.getUserId().toString().equals(userId)) {
                    haveApplied = true;
                }
                if (item.getUserToken() != null && item.getUserToken().equals(userId)) {
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
                    .map(this::convertSubqueueItem)
                    .toArray(WSSpeechSubqueueItem[]::new);
            wsSubqueue.setApplied(items);
        } else {
            wsSubqueue.setApplied(null);
        }

        return wsSubqueue;
    }

    private WSSpeechSubqueueItem convertSubqueueItem(MQSpeechSubqueueItem item) {
        WSSpeechSubqueueItem wsItem = new WSSpeechSubqueueItem(item.getId());
        wsItem.setName(item.getName());
        wsItem.setPosition(item.getPosition());
        wsItem.setPointOfOrder(item.isPointOfOrder());
        wsItem.setAppliedAt(item.getDateApplied());

        return wsItem;
    }

    private WSSpeechActiveSlot convertActiveSlot(MQSpeechQueueActiveSlot activeSlot) {
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
