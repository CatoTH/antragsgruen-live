package de.antragsgruen.live;

import de.antragsgruen.live.mapper.SpeechUserMapper;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSSpeechQueue;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeechUserHandler {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    private String[] findRelevantUserIds(String subdomain, String consultation)
    {
        // 1) First find all subscriptions with a destination matching /user/[site]/[consultation]/[userid]/speech
        // 2) Extract the User ID from the destinations
        // 3) Return unique User IDs (we only need to send messages to each user once)
        return userRegistry.findSubscriptions(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return parts.length == 6 && "user".equals(parts[1]) && "speech".equals(parts[5])
                    && subdomain.equals(parts[2]) && consultation.equals(parts[3]);
        }).stream().map(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return parts[4];
        }).distinct().toArray(String[]::new);
    }

    public void onSpeechEvent(String subdomain, String consultation, MQSpeechQueue mqQueue) {
        String[] users = findRelevantUserIds(subdomain, consultation);

        log.info("Sending speech event to " + users.length + " (out of " + userRegistry.getUserCount() + ") user(s)");

        for (String userId : users) {
            WSSpeechQueue wsQueue = SpeechUserMapper.convertQueue(mqQueue, userId);

            sender.sendToUser(subdomain, consultation, userId, Sender.USER_CHANNEL_SPEECH, wsQueue);
        }
    }
}
