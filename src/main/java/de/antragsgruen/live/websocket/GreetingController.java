package de.antragsgruen.live.websocket;

import de.antragsgruen.live.websocket.dto.WSGreeting;
import de.antragsgruen.live.websocket.dto.WSHelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public WSGreeting greeting(WSHelloMessage message, Principal principal) throws Exception {
        log.warn("Received message: " + message.getName() + " (from " + principal.getName() + ")");

        return new WSGreeting("[From: " + principal.getName() + "] Hello, " + message.getName());
    }
}

