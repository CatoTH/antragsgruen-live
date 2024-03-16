package de.antragsgruen.live.multisite;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@RequiredArgsConstructor
public class AntragsgruenJwtDecoder {
    private final JwtDecoder jwtDecoder;

    public static AntragsgruenJwtDecoder create(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyString = publicKey
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] keyBytes = Base64.decodeBase64(publicKeyString);
        EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) kf.generatePublic(keySpec);

        return new AntragsgruenJwtDecoder(
                NimbusJwtDecoder.withPublicKey(rsaPublicKey).build()
        );
    }

    public JwtAuthenticationToken getJwtAuthToken(String token) {
        Jwt jwt = this.jwtDecoder.decode(token);

        return new JwtAuthenticationToken(jwt);
    }
}
