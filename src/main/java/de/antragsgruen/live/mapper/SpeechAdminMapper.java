package de.antragsgruen.live.mapper;

import de.antragsgruen.live.rabbitmq.dto.*;
import de.antragsgruen.live.websocket.dto.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class SpeechAdminMapper {
    private SpeechAdminMapper() {
        throw new UnsupportedOperationException();
    }

    public static WSSpeechQueueAdmin convertQueue(MQSpeechQueue queue, @Nullable String language, @Nullable String defaultLanguage) {
        WSSpeechSubqueueAdmin[] wsSubqueues = Stream
                .of(queue.subqueues())
                .map(subqueue -> convertSubqueue(subqueue, language, defaultLanguage))
                .toArray(WSSpeechSubqueueAdmin[]::new);

        WSSpeechActiveSlot[] wsActiveSlots = Stream
                .of(queue.slots())
                .filter(slot -> slot.dateStarted() != null)
                .map(slot -> convertActiveSlot(slot, language, defaultLanguage))
                .toArray(WSSpeechActiveSlot[]::new);

        WSSpeechQueueSettingsAdmin settings = SpeechAdminMapper.convertSettings(queue.settings());

        return new WSSpeechQueueAdmin(
                queue.id(),
                queue.isActive(),
                settings,
                wsSubqueues,
                wsActiveSlots,
                queue.otherActiveName().resolve(language, defaultLanguage),
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

    private static WSSpeechSubqueueAdmin convertSubqueue(
            MQSpeechSubqueue subqueue,
            @Nullable String language,
            @Nullable String defaultLanguage
    ) {
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
                subqueue.name().resolve(language, defaultLanguage),
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
