package de.antragsgruen.live.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class StompTestConnection {
    final private Resource privateKeyFilename;
    final private int port;

    @Nullable private Map<String, Object> receivedMessage;
    final FutureTask<Object> onMessageReceived = new FutureTask<>(() -> {}, new Object());

    @Nullable private WebSocketStompClient stompClient;
    @Nullable private StompSession stompSession;
    @Nullable FutureTask<String> onError;

    public StompTestConnection(int port, Resource privateKeyFilename) {
        this.port = port;
        this.privateKeyFilename = privateKeyFilename;
    }

    private RSAPrivateKey getJwtPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        InputStream is = privateKeyFilename.getInputStream();
        String privateKeyString = StreamUtils
                .copyToString(is, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] keyBytes = Base64.decodeBase64(privateKeyString);
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }

    private String generateJwt(String installation, String site, String consultation, String userId, @Nullable List<String> roles) {
        RSAPrivateKey privateKey;
        try {
            privateKey = this.getJwtPrivateKey();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("site", site);
        payload.put("consultation", consultation);

        if (roles != null) {
            payload.put("roles", roles);
        }

        Date now = new Date();
        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .issuer(installation)
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + 1000*60*10))
                .subject(userId)
                .claim("payload", payload)
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJwt = new SignedJWT(header, jwtClaims);
        try {
            signedJwt.sign(new RSASSASigner(privateKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return signedJwt.serialize();
    }

    public FutureTask<StompSession> connect(String installation, String site, String consultation, String userId, @Nullable List<String> roles) {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        String url = "ws://localhost:" + port + "/websocket";

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();

        StompHeaders headers = new StompHeaders();
        headers.set("jwt", generateJwt(installation, site, consultation, userId, roles));
        headers.set("installation", installation);

        StompTestSessionHandler sessionHandler = new StompTestSessionHandler();
        stompClient.connect(url, handshakeHeaders, headers, sessionHandler);
        this.onError = sessionHandler.onError();

        return sessionHandler.onConnect();
    }

    public void connectAndWait(String installation, String site, String consultation, String userId, @Nullable List<String> roles) {
        try {
            this.stompSession = this.connect(installation, site, consultation, userId, roles).get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Could not connect to STOMP within a reasonable amount of time");
        }
    }

    public FutureTask<String> connectAndExpectError(String installation, String site, String consultation, String userId, @Nullable List<String> roles) {
        this.connect(installation, site, consultation, userId, roles);
        return this.onError;
    }

    public void subscribe(String topic) {
        this.stompSession.subscribe(topic, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Map.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                receivedMessage = (Map<String, Object>) payload;
                onMessageReceived.run();
            }
        });
        // @TODO Figure out how to wait for a receipt and trigger a onSubscribed task
    }

    public FutureTask<String> subscribeAndExpectError(String topic) {
        this.subscribe(topic);
        return this.onError;
    }

    public Map<String, Object> waitForMessageReceived() {
        try {
            this.onMessageReceived.get(5, TimeUnit.SECONDS);
            if (this.receivedMessage == null) {
                throw new RuntimeException("No message was received");
            }
            return this.receivedMessage;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
