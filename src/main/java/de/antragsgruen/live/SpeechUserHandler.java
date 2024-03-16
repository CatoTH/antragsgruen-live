package de.antragsgruen.live;

import de.antragsgruen.live.mapper.SpeechUserMapper;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSSpeechQueueUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public final class SpeechUserHandler extends LiveHandlerBase {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    public void onSpeechEvent(ConsultationScope scope, MQSpeechQueue mqQueue) {
        String[] users = findRelevantUserIds(userRegistry, scope, Sender.ROLE_USER, Sender.USER_CHANNEL_SPEECH);

        log.info("Sending speech user event to " + users.length + " (out of " + userRegistry.getUserCount() + ") user(s)");

        for (String userId : users) {
            WSSpeechQueueUser wsQueue = SpeechUserMapper.convertQueue(mqQueue, userId);

            sender.sendToUser(scope, userId, Sender.ROLE_USER, Sender.USER_CHANNEL_SPEECH, wsQueue);
        }
    }
}
