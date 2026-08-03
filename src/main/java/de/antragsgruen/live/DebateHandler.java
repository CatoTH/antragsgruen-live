package de.antragsgruen.live;

import de.antragsgruen.live.mapper.DebateMapper;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQDebateState;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSDebateState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public final class DebateHandler extends LiveHandlerBase {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    public void onDebateEvent(ConsultationScope scope, MQDebateState mqQueue) {
        String[] users = findRelevantUserIds(userRegistry, scope, Sender.ROLE_USER, Sender.USER_CHANNEL_DEBATE);

        log.info("Sending debate user event to " + users.length + " (out of " + userRegistry.getUserCount() + ") user(s)");

        for (String userId : users) {
            WSDebateState wsState = DebateMapper.convertState(mqQueue, userId);

            sender.sendToUser(scope, userId, Sender.ROLE_USER, Sender.USER_CHANNEL_DEBATE, wsState);
        }
    }
}
