package de.antragsgruen.live.websocket;

import de.antragsgruen.live.multisite.ConsultationScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TopicPermissionChecker {
    private static final String ROLE_SPEECH_ADMIN = "ROLE_SPEECH_ADMIN";

    public static final int USER_PARTS_LENGTH = 7; // Also used for /admin/ topics
    public static final int USER_PART_ROLE = 1;
    public static final int USER_PART_INSTALLATION = 2;
    public static final int USER_PART_SITE = 3;
    public static final int USER_PART_CONSULTATION = 4;
    public static final int USER_PART_USER = 5;
    public static final int USER_PART_MODULE = 6;

    public static final int TOPIC_PARTS_LENGTH = 6;
    public static final int TOPIC_PART_TOPIC = 1;
    public static final int TOPIC_PART_INSTALLATION = 2;
    public static final int TOPIC_PART_SITE = 3;
    public static final int TOPIC_PART_CONSULTATION = 4;

    /**
     * Supported destination patterns:
     * - /user/[installation]/[subdomain]/[consultation]/[userid]/speech
     * - /admin/[installation]/[subdomain]/[consultation]/[userid]/speech
     * - /topic/[installation]/[subdomain]/[consultation]/[...]
     */
    public boolean canSubscribeToDestination(JwtAuthenticationToken jwtToken, @Nullable String destination) {
        if (destination == null) {
            return false;
        }

        String[] pathParts = destination.split("/");
        if (!"".equals(pathParts[0])) {
            return false;
        }

        ConsultationScope scope = TopicPermissionChecker.consultationScopeFromPathParts(pathParts);

        boolean additionalPermissionsPassed = true;
        if (Sender.ROLE_USER.equals(pathParts[USER_PART_ROLE]) && pathParts.length == USER_PARTS_LENGTH) {
            additionalPermissionsPassed = pathParts[USER_PART_USER].equals(jwtToken.getName());
        }
        if (Sender.ROLE_ADMIN.equals(pathParts[USER_PART_ROLE]) && pathParts.length == USER_PARTS_LENGTH) {
            additionalPermissionsPassed = jwtHasRoleForTopic(jwtToken, pathParts[USER_PART_MODULE])
                    && pathParts[USER_PART_USER].equals(jwtToken.getName());
        }

        return (scope != null && jwtIsForCorrectConsultation(jwtToken, scope) && additionalPermissionsPassed);
    }

    public static ConsultationScope consultationScopeFromPathParts(String[] pathParts) {
        if ("topic".equals(pathParts[TOPIC_PART_TOPIC]) && pathParts.length == TOPIC_PARTS_LENGTH) {
            return new ConsultationScope(pathParts[TOPIC_PART_INSTALLATION], pathParts[TOPIC_PART_SITE], pathParts[TOPIC_PART_CONSULTATION]);
        }
        if (Sender.ROLE_USER.equals(pathParts[USER_PART_ROLE]) && pathParts.length == USER_PARTS_LENGTH) {
            return new ConsultationScope(pathParts[USER_PART_INSTALLATION], pathParts[USER_PART_SITE], pathParts[USER_PART_CONSULTATION]);
        }
        if (Sender.ROLE_ADMIN.equals(pathParts[USER_PART_ROLE]) && pathParts.length == USER_PARTS_LENGTH) {
            return new ConsultationScope(pathParts[USER_PART_INSTALLATION], pathParts[USER_PART_SITE], pathParts[USER_PART_CONSULTATION]);
        }
        return null;
    }

    private boolean jwtIsForCorrectConsultation(JwtAuthenticationToken jwtToken, ConsultationScope scope) {
        Object payload = jwtToken.getTokenAttributes().get("payload");
        if (!(payload instanceof Map<?, ?> payloadMap)) {
            log.warn("No payload found");
            return false;
        }

        String issuer = jwtToken.getToken().getClaim("iss");
        if (issuer == null || issuer.isEmpty() || !issuer.equals(scope.installation())) {
            log.warn("Incorrect installation provided: " + scope.installation(), payloadMap);
            return false;
        }

        if (!payloadMap.containsKey("site") || payloadMap.get("site") == null || !payloadMap.get("site").equals(scope.site())) {
            log.warn("Incorrect site provided: " + scope.site(), payloadMap);
            return false;
        }

        if (!payloadMap.containsKey("consultation") || payloadMap.get("consultation") == null
                || !payloadMap.get("consultation").equals(scope.consultation())) {
            log.warn("Incorrect consultation provided: " + scope.consultation(), payloadMap);
            return false;
        }

        return true;
    }

    private boolean jwtHasRoleForTopic(JwtAuthenticationToken jwtToken, String topic) {
        Object payload = jwtToken.getTokenAttributes().get("payload");
        if (!(payload instanceof Map<?, ?> payloadMap) || !payloadMap.containsKey("roles")) {
            log.warn("No payload found");
            return false;
        }

        Object roles = payloadMap.get("roles");
        if (!(roles instanceof List<?> rolesArray)) {
            log.warn("No roles found");
            return false;
        }

        if (getNecessaryRoleForTopic(topic) == null) {
            log.warn("No role for this topic found");
            return false;
        }

        return rolesArray.contains(getNecessaryRoleForTopic(topic));
    }

    private @Nullable String getNecessaryRoleForTopic(String topic) {
        if (Sender.USER_CHANNEL_SPEECH.equals(topic)) {
            return TopicPermissionChecker.ROLE_SPEECH_ADMIN;
        }
        return null;
    }
}
