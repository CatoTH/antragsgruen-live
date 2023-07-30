package de.antragsgruen.live.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.messaging.converter.StringMessageConverter;
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
import java.util.concurrent.FutureTask;

public class StompTestConnection {
    final private Resource privateKeyFilename;
    final private int port;
    final FutureTask<Object> onConnectFuture = new FutureTask<>(() -> {}, new Object());

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

    private String generateJwt(String site, String consultation, int userId) {
        RSAPrivateKey privateKey;
        try {
            privateKey = this.getJwtPrivateKey();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        Date now = new Date();
        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .issuer("https://test.antragsgruen.de") // @TODO
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + 1000*60*10))
                .subject(Integer.toString(userId))
                // .claim('payload', ) @TODO
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

    public FutureTask<Object> connect(String site, String consultation, int userId) {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());
        String url = "ws://localhost:" + port + "/websocket";

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();

        StompHeaders headers = new StompHeaders();
        headers.set("jwt", generateJwt(site, consultation, userId));

        StompSessionHandler sessionHandler = new StompTestSessionHandler(onConnectFuture);
        stompClient.connect(url, handshakeHeaders, headers, sessionHandler);

        return onConnectFuture;
    }
}
