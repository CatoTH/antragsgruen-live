package de.antragsgruen.live.utils;

import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.FutureTask;

public class StompTestSessionHandler implements StompSessionHandler {
    @Nullable private FutureTask<StompSession> onConnectFuture;
    @Nullable private FutureTask<String> onErrorFuture;

    @Nullable String lastStompError;
    @Nullable private StompSession stompSession;

    public StompTestSessionHandler() {
    }

    public FutureTask<StompSession> onConnect()
    {
        this.onConnectFuture = new FutureTask<>(() -> this.stompSession);
        return this.onConnectFuture;
    }

    public FutureTask<String> onError()
    {
        this.onErrorFuture = new FutureTask<>(() -> this.lastStompError);
        return this.onErrorFuture;
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
        this.lastStompError = exception.getMessage();
        this.onErrorFuture.run();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        List<String> messages = headers.get("message");
        for (String message: messages) {
            this.lastStompError = message;
            this.onErrorFuture.run();
        }
        //System.out.println(headers);
        //            System.out.println(payload);
        //            System.out.println("Frame");
    }
}
