package de.antragsgruen.live.multisite;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AntragsgruenInstallation {
    private final String installationId;

    private final AntragsgruenJwtDecoder jwtDecoder;
}
