package de.antragsgruen.live.websocket;

import de.antragsgruen.live.websocket.dto.Greeting;
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

    public void sendToUser(String site, String consultation, String user, String message)
    {
        String target = "/user/" + site + "/" + consultation + "/" + user + "/update";
        Greeting object = new Greeting(message);

        logger.debug("Sending to: " + target + " / " + object.getContent());

        this.messagingTemplate.convertAndSend(target, object);
    }

    public void sendToConsultation(String site, String consultation, String message)
    {
        String target = "/topic/" + site + "/" + consultation + "/update";
        Greeting object = new Greeting(message);
        this.messagingTemplate.convertAndSend(target, object);
    }
}
