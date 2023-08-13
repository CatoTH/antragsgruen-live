package de.antragsgruen.live.utils;

import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.concurrent.FutureTask;

public class StompTestSessionHandler implements StompSessionHandler {
    @Nullable private FutureTask<StompSession> onConnectFuture;
    @Nullable private StompSession stompSession;

    public StompTestSessionHandler() {
    }

    public FutureTask<StompSession> onConnect()
    {
         this.onConnectFuture = new FutureTask<>(() -> this.stompSession);
         return this.onConnectFuture;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.stompSession = session;
        this.onConnectFuture.run();
    }

    @Override
    public void handleException(StompSession session, @Nullable StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Exception: " + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("Transport error: " + exception.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        System.out.println(headers);
        System.out.println(payload);
        System.out.println("Frame");
    }
}
