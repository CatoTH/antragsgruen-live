package de.antragsgruen.live.multisite;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AntragsgruenInstallationProvider {
    private final Environment environment;
    private final HashMap<String, AntragsgruenInstallation> installations = new HashMap<>();

    public AntragsgruenInstallation getInstallation(String installationId) throws InstallationNotFoundException {
        if (!installations.containsKey(installationId)) {
            throw new InstallationNotFoundException("Invalid installation id: " + installationId);
        }

        return installations.get(installationId);
    }

    @PostConstruct
    public void onInit() {
        String installationId;
        String publicKey;
        int count = 0;
        do {
            installationId = environment.getProperty("antragsgruen.installations." + count + ".id");
            publicKey = environment.getProperty("antragsgruen.installations." + count + ".public-key");

            if (installationId == null && publicKey == null) {
                return;
            }
            if (installationId == null || publicKey == null || installationId.isEmpty() || publicKey.isEmpty()) {
                throw new RuntimeException("ANTRAGSGRUEN_INSTALLATION_" + count + "_* is inconsistently filled");
            }

            try {
                this.installations.put(installationId, AntragsgruenInstallationProvider.createInstallation(installationId, publicKey));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
            log.info("Found installation ID: " + installationId);

            count++;
        } while (true);
    }

    private static AntragsgruenInstallation createInstallation(String installationId, String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new AntragsgruenInstallation(
                installationId,
                AntragsgruenJwtDecoder.create(publicKey)
        );
    }
}
