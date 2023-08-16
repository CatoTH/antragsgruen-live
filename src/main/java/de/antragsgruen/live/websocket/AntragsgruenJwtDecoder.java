package de.antragsgruen.live.websocket;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service
@Slf4j
public class AntragsgruenJwtDecoder {
    @Value("classpath:${antragsgruen.jwt.key.public}")
    private Resource publicKeyFilename;

    private JwtDecoder jwtDecoder;

    @PostConstruct
    public void loadPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        InputStream is = this.publicKeyFilename.getInputStream();
        String publicKeyString = StreamUtils
                .copyToString(is, StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] keyBytes = Base64.decodeBase64(publicKeyString);
        EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpec);

        this.jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    public JwtAuthenticationToken getJwtAuthToken(String token)
    {
        Jwt jwt = this.jwtDecoder.decode(token);

        return new JwtAuthenticationToken(jwt);
    }
}
