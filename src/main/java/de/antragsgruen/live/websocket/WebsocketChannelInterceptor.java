package de.antragsgruen.live.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebsocketChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private AntragsgruenJwtDecoder jwtDecoder;

    Logger logger = LoggerFactory.getLogger(WebsocketChannelInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) throws MessagingException {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (headerAccessor == null) {
            throw new MessagingException("Could not authenticate JWT: accessor is null");
        }

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            this.onConnect(message, headerAccessor);
        }

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            this.onSubscribe(message, headerAccessor);
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        logger.warn("preReceive", message);

        return message;
    }

    private void onConnect(Message<?> message, StompHeaderAccessor headerAccessor) throws MessagingException
    {
        for (String head: headerAccessor.getNativeHeader("jwt")) {
            try {
                JwtAuthenticationToken token = this.jwtDecoder.getJwtAuthToken(head);
                headerAccessor.setUser(token);
                logger.info("Connected Websocket: " + token.getName());
            } catch (Exception e) {
                throw new MessagingException("Could not authenticate JWT: " + e.getMessage());
            }
        }
    }

    private void onSubscribe(Message<?> message, StompHeaderAccessor headerAccessor) throws MessagingException
    {
        Principal userPrincipal = headerAccessor.getUser();

        if (!(userPrincipal instanceof JwtAuthenticationToken)) {
            throw new MessagingException("Not (correctly) logged in");
        }
        if (headerAccessor.getDestination() == null) {
            throw new MessagingException("No destination provided");
        }


        if (!canSubscribeToDestination((JwtAuthenticationToken) userPrincipal, headerAccessor.getDestination())) {
            logger.warn("Attempted invalid subscription: " + userPrincipal.getName() + " => " + headerAccessor.getDestination());
            throw new MessagingException("Forbidden to subscribe to this destination");
        }

        logger.info("Subscribed: " + userPrincipal.getName() + " => " + headerAccessor.getDestination());
    }

    /**
     * Supported destination patterns:
     * - /user/[subdomain]/[consultation]/[username]/[...]
     * - /topic/[subdomain]/[consultation]/[...]
     */
    private boolean canSubscribeToDestination(JwtAuthenticationToken jwtToken, String destination)
    {
        String[] pathParts = destination.split("/");
        if (!"".equals(pathParts[0])) {
            return false;
        }

        // @TODO Check subdomain / consultation access
        if ("topic".equals(pathParts[1]) && pathParts.length == 5) {
            return true;
        }
        if ("user".equals(pathParts[1]) && pathParts.length == 6) {
            return pathParts[4].equals(jwtToken.getName());
        }

        return false;
    }
}
