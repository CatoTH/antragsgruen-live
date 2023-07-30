package de.antragsgruen.live.utils;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.concurrent.FutureTask;

public class StompTestSessionHandler implements StompSessionHandler {
    private FutureTask<Object> onConnectFuture;

    public StompTestSessionHandler(FutureTask<Object> onConnectFuture) {
        this.onConnectFuture = onConnectFuture;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.onConnectFuture.run(); // We're done
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Exception: " + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("Transport error: " + exception.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println(headers);
        System.out.println(payload);
        System.out.println("Frame");
    }
}
