package de.antragsgruen.live.websocket;

import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.websocket.dto.WSGreeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sender {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public static final String USER_CHANNEL_DEFAULT = "default";
    public static final String USER_CHANNEL_SPEECH = "speech";
    public static final String USER_CHANNEL_DEBATE = "debate";
    public static final String USER_CHANNEL_VOTING = "voting";

    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";

    /**
     * Sends to the destination a subscriber is subscribed to, which includes the language they read
     * in - so that two tabs of the same person in two languages each get their own wording.
     *
     * @param language the language of the destination, null for a client that did not state one
     */
    public void sendToUser(ConsultationScope scope, String user, @Nullable String language, String role, String channel, Object message) {
        String target = "/" + role + "/" + scope.installation() + "/" + scope.site() + "/" + scope.consultation() + "/" + user + "/" + channel;
        if (language != null) {
            target += "/" + language;
        }

        log.debug("Sending to: " + target + " / " + message.toString());

        this.messagingTemplate.convertAndSend(target, message);
    }

    public void sendToConsultation(ConsultationScope scope, String message) {
        String target = "/topic/" + scope.installation() + "/" + scope.site() + "/" + scope.consultation() + "/update";
        WSGreeting object = new WSGreeting(message);
        this.messagingTemplate.convertAndSend(target, object);
    }
}
