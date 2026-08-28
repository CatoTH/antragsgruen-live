package de.antragsgruen.live;

import de.antragsgruen.live.mapper.SpeechAdminMapper;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSSpeechQueueAdmin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public final class SpeechAdminHandler extends LiveHandlerBase {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    public void onSpeechEvent(ConsultationScope scope, MQSpeechQueue mqQueue, @Nullable String defaultLanguage) {
        Collection<Subscriber> subscribers = findRelevantSubscribers(userRegistry, scope, Sender.ROLE_ADMIN, Sender.USER_CHANNEL_SPEECH);

        log.info("Sending speech admin event to " + subscribers.size() + " (out of " + userRegistry.getUserCount() + ") user(s)");

        for (Subscriber subscriber : subscribers) {
            WSSpeechQueueAdmin wsQueue = SpeechAdminMapper.convertQueue(mqQueue, subscriber.language(), defaultLanguage);

            sender.sendToUser(scope, subscriber.userId(), Sender.ROLE_ADMIN, Sender.USER_CHANNEL_SPEECH, wsQueue);
        }
    }
}
