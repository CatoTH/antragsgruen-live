package de.antragsgruen.live;

import de.antragsgruen.live.mapper.DebateMapper;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQDebateState;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSDebateState;
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
public final class DebateHandler extends LiveHandlerBase {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    public void onDebateEvent(ConsultationScope scope, MQDebateState mqQueue, @Nullable String defaultLanguage) {
        Collection<Subscriber> subscribers = findRelevantSubscribers(userRegistry, scope, Sender.ROLE_USER, Sender.USER_CHANNEL_DEBATE);

        log.info("Sending debate user event to " + subscribers.size() + " (out of " + userRegistry.getUserCount() + ") user(s)");

        for (Subscriber subscriber : subscribers) {
            WSDebateState wsState = DebateMapper.convertState(mqQueue, subscriber.language(), defaultLanguage);

            sender.sendToUser(scope, subscriber.userId(), Sender.ROLE_USER, Sender.USER_CHANNEL_DEBATE, wsState);
        }
    }
}
