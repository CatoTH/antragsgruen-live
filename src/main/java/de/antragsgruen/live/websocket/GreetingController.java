package de.antragsgruen.live.websocket;

import de.antragsgruen.live.websocket.dto.Greeting;
import de.antragsgruen.live.websocket.dto.HelloMessage;
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
    public Greeting greeting(HelloMessage message, Principal principal) throws Exception
    {
        logger.warn("Received message: " + message.getName() + " (from " + principal.getName() + ")");

        Thread.sleep(1000);
        return new Greeting("[From: " + principal.getName() + "] Hello, " + message.getName());
    }
}

