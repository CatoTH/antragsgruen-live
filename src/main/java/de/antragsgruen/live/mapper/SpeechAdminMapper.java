package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.*;
import de.antragsgruen.live.websocket.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SpeechAdminMapper {
    public static WSSpeechQueueAdmin convertQueue(MQSpeechQueue queue) {
        WSSpeechSubqueueAdmin[] wsSubqueues = Stream
                .of(queue.subqueues())
                .map(SpeechAdminMapper::convertSubqueue)
                .toArray(WSSpeechSubqueueAdmin[]::new);

        WSSpeechActiveSlot[] wsActiveSlots = Stream
                .of(queue.slots())
                .filter(slot -> slot.dateStarted() != null)
                .map(SpeechAdminMapper::convertActiveSlot)
                .toArray(WSSpeechActiveSlot[]::new);

        WSSpeechQueueSettingsAdmin settings = SpeechAdminMapper.convertSettings(queue.settings());

        return new WSSpeechQueueAdmin(
                queue.id(),
                queue.isActive(),
                settings,
                wsSubqueues,
                wsActiveSlots,
                queue.otherActiveName(),
                queue.currentTime()
        );
    }

    private static WSSpeechQueueSettingsAdmin convertSettings(MQSpeechQueueSettings settings) {
        return new WSSpeechQueueSettingsAdmin(
                settings.isOpen(),
                settings.isOpenPoo(),
                settings.allowCustomNames(),
                settings.preferNonspeaker(),
                settings.showNames(),
                settings.speakingTime()
        );
    }

    private static WSSpeechSubqueueAdmin convertSubqueue(MQSpeechSubqueue subqueue) {
        List<WSSpeechSubqueueAdminItem> onlist = new ArrayList<>();
        List<WSSpeechSubqueueAdminItem> applied = new ArrayList<>();

        for (MQSpeechSubqueueItem item : subqueue.items()) {
            WSSpeechSubqueueAdminItem wsItem = SpeechAdminMapper.convertSubqueueItem(item);
            int position = Optional.ofNullable(item.position()).orElse(0);
            if (position > 0) {
                onlist.add(wsItem);
            }
            if (position < 0) {
                applied.add(wsItem);
            }
        }

        return new WSSpeechSubqueueAdmin(
                subqueue.id(),
                subqueue.name(),
                onlist.toArray(WSSpeechSubqueueAdminItem[]::new),
                applied.toArray(WSSpeechSubqueueAdminItem[]::new)
        );
    }

    private static WSSpeechSubqueueAdminItem convertSubqueueItem(MQSpeechSubqueueItem item) {
        return new WSSpeechSubqueueAdminItem(
                item.id(),
                item.name(),
                item.userId(),
                item.userToken(),
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
