package de.antragsgruen.live;

import de.antragsgruen.live.mapper.SpeechAdminMapper;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSSpeechQueueAdmin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechAdminHandler extends HandlerBase {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    public void onSpeechEvent(String subdomain, String consultation, MQSpeechQueue mqQueue) {
        String[] users = findRelevantUserIds(userRegistry, subdomain, consultation, Sender.ROLE_ADMIN, Sender.USER_CHANNEL_SPEECH);

        log.info("Sending speech admin event to " + users.length + " (out of " + userRegistry.getUserCount() + ") user(s)");

        for (String userId : users) {
            WSSpeechQueueAdmin wsQueue = SpeechAdminMapper.convertQueue(mqQueue);

            sender.sendToUser(subdomain, consultation, userId, Sender.ROLE_ADMIN, Sender.USER_CHANNEL_SPEECH, wsQueue);
        }
    }
}
