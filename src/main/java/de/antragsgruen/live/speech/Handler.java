package de.antragsgruen.live.speech;

import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSSpeechQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
public class Handler {
    @Autowired
    private Sender sender;

    @Autowired
    private SimpUserRegistry userRegistry;

    private Logger logger = LoggerFactory.getLogger(Handler.class);

    private WSSpeechQueue convertSpeechQueue(MQSpeechQueue queue, String userId) {
        WSSpeechQueue wsDto = new WSSpeechQueue(userId);
        return wsDto;
    }

    public void onSpeechEvent(String subdomain, String consultation, MQSpeechQueue mqQueue) {
        logger.info("Sending speech event to " + userRegistry.getUserCount() + " users");
        for (SimpUser user : userRegistry.getUsers()) {
            WSSpeechQueue wsQueue = convertSpeechQueue(mqQueue, user.getName());
            sender.sendToUser(subdomain, consultation, user.getName(), Sender.USER_CHANNEL_SPEECH, wsQueue);
        }
    }
}
