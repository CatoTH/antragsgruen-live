package de.antragsgruen.live.websocket;

import de.antragsgruen.live.multisite.AntragsgruenInstallation;
import de.antragsgruen.live.multisite.AntragsgruenInstallationProvider;
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
public final class WebsocketChannelInterceptor implements ChannelInterceptor {
    @NonNull private final TopicPermissionChecker topicPermissionChecker;
    @NonNull private final AntragsgruenInstallationProvider installationProvider;

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
        log.warn("postReceive", message);

        return message;
    }

    private AntragsgruenInstallation onConnectGetInstallation(StompHeaderAccessor headerAccessor) throws Exception {
        List<String> jwtHeaders = headerAccessor.getNativeHeader("installation");
        jwtHeaders = Optional.ofNullable(jwtHeaders).orElse(new ArrayList<>());

        for (String head: jwtHeaders) {
            return this.installationProvider.getInstallation(head);
        }

        throw new Exception("No installation header found");
    }

    private JwtAuthenticationToken onConnectGetAuthenticatedToken(AntragsgruenInstallation installation, StompHeaderAccessor headerAccessor) throws Exception {
        List<String> jwtHeaders = headerAccessor.getNativeHeader("jwt");
        jwtHeaders = Optional.ofNullable(jwtHeaders).orElse(new ArrayList<>());

        for (String head: jwtHeaders) {
            return installation.getJwtDecoder().getJwtAuthToken(head);
        }

        throw new Exception("No jwt header found");
    }

    private void onConnect(Message<?> message, StompHeaderAccessor headerAccessor) throws MessagingException {
        try {
            AntragsgruenInstallation installation = this.onConnectGetInstallation(headerAccessor);
            JwtAuthenticationToken token = this.onConnectGetAuthenticatedToken(installation, headerAccessor);
            headerAccessor.setUser(token);
            log.info("Connected Websocket: " + token.getName());
        } catch (Exception e) {
            log.warn("Could not authenticate JWT: " + e.getMessage());
            throw new MessagingException("Could not authenticate JWT: " + e.getMessage());
        }
    }

    private void onSubscribe(Message<?> message, StompHeaderAccessor headerAccessor) throws MessagingException {
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
