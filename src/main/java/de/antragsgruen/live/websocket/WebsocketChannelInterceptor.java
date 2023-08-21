package de.antragsgruen.live.websocket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketChannelInterceptor implements ChannelInterceptor {
    @NonNull private AntragsgruenJwtDecoder jwtDecoder;
    @NonNull private TopicPermissionChecker topicPermissionChecker;

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
        log.warn("preReceive", message);

        return message;
    }

    private void onConnect(Message<?> message, StompHeaderAccessor headerAccessor) throws MessagingException
    {
        List<String> jwtHeaders = headerAccessor.getNativeHeader("jwt");
        jwtHeaders = Optional.ofNullable(jwtHeaders).orElse(new ArrayList<>());

        for (String head: jwtHeaders) {
            try {
                JwtAuthenticationToken token = this.jwtDecoder.getJwtAuthToken(head);
                headerAccessor.setUser(token);
                log.info("Connected Websocket: " + token.getName());
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


        if (!topicPermissionChecker.canSubscribeToDestination((JwtAuthenticationToken) userPrincipal, headerAccessor.getDestination())) {
            log.warn("Attempted invalid subscription: " + userPrincipal.getName() + " => " + headerAccessor.getDestination());
            throw new MessagingException("Forbidden to subscribe to this destination");
        }

        log.info("Subscribed: " + userPrincipal.getName() + " => " + headerAccessor.getDestination());
    }


}
