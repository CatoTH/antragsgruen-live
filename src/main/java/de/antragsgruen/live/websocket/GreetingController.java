package de.antragsgruen.live.websocket;

import de.antragsgruen.live.websocket.dto.WSGreeting;
import de.antragsgruen.live.websocket.dto.WSHelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GreetingController {
    Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public WSGreeting greeting(WSHelloMessage message, Principal principal) throws Exception
    {
        logger.warn("Received message: " + message.getName() + " (from " + principal.getName() + ")");

        Thread.sleep(1000);
        return new WSGreeting("[From: " + principal.getName() + "] Hello, " + message.getName());
    }
}

