package de.antragsgruen.live.websocket;

import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.websocket.dto.WSGreeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sender {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public static final String USER_CHANNEL_DEFAULT = "default";
    public static final String USER_CHANNEL_SPEECH = "speech";

    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";

    public void sendToUser(ConsultationScope scope, String user, String role, String channel, Object message) {
        String target = "/" + role + "/" + scope.installation() + "/" + scope.site() + "/" + scope.consultation() + "/" + user + "/" + channel;

        log.debug("Sending to: " + target + " / " + message.toString());

        this.messagingTemplate.convertAndSend(target, message);
    }

    public void sendToConsultation(ConsultationScope scope, String message) {
        String target = "/topic/" + scope.installation() + "/" + scope.site() + "/" + scope.consultation() + "/update";
        WSGreeting object = new WSGreeting(message);
        this.messagingTemplate.convertAndSend(target, object);
    }
}
