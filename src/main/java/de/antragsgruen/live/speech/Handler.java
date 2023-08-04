package de.antragsgruen.live.speech;

import de.antragsgruen.live.rabbitmq.dto.SpeechQueue;
import de.antragsgruen.live.websocket.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Handler {
    @Autowired
    private Sender sender;

    public void onSpeechEvent(String subdomain, String consultation, SpeechQueue event) {
        sender.sendToConsultation(subdomain, consultation, event.getId() + "");
    }
}
