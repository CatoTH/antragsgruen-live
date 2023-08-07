package de.antragsgruen.live.websocket;

import de.antragsgruen.live.websocket.dto.WSGreeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    Logger logger = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public static final String USER_CHANNEL_DEFAULT = "default";
    public static final String USER_CHANNEL_SPEECH = "speech";

    public void sendToUser(String site, String consultation, String user, String channel, Object message)
    {
        String target = "/user/" + site + "/" + consultation + "/" + user + "/" + channel;

        logger.debug("Sending to: " + target + " / " + message.toString());

        this.messagingTemplate.convertAndSend(target, message);
    }

    public void sendToConsultation(String site, String consultation, String message)
    {
        String target = "/topic/" + site + "/" + consultation + "/update";
        WSGreeting object = new WSGreeting(message);
        this.messagingTemplate.convertAndSend(target, object);
    }
}
