package de.antragsgruen.live.websocket;

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

    public static final int USER_PARTS_LENGTH = 6;
    public static final int USER_PART_ROLE = 1;
    public static final int USER_PART_SITE = 2;
    public static final int USER_PART_CONSULTATION = 3;
    public static final int USER_PART_USER = 4;
    public static final int USER_PART_MODULE = 5;

    public static final int TOPIC_PARTS_LENGTH = 5;
    public static final int TOPIC_PART_TOPIC = 1;
    public static final int TOPIC_PART_SITE = 2;
    public static final int TOPIC_PART_CONSULTATION = 3;

    /**
     * Supported destination patterns:
     * - /user/[subdomain]/[consultation]/[userid]/speech
     * - /adin/[subdomain]/[consultation]/[userid]/speech
     * - /topic/[subdomain]/[consultation]/[...]
     */
    public boolean canSubscribeToDestination(JwtAuthenticationToken jwtToken, @Nullable String destination) {
        if (destination == null) {
            return false;
        }

        String[] pathParts = destination.split("/");
        if (!"".equals(pathParts[0])) {
            return false;
        }

        if ("topic".equals(pathParts[TOPIC_PART_TOPIC]) && pathParts.length == TOPIC_PARTS_LENGTH) {
            return jwtIsForCorrectSiteAndConsultation(jwtToken, pathParts[TOPIC_PART_SITE], pathParts[TOPIC_PART_CONSULTATION]);
        }
        if (Sender.ROLE_USER.equals(pathParts[USER_PART_ROLE]) && pathParts.length == USER_PARTS_LENGTH) {
            return jwtIsForCorrectSiteAndConsultation(jwtToken, pathParts[USER_PART_SITE], pathParts[USER_PART_CONSULTATION])
                    && pathParts[USER_PART_USER].equals(jwtToken.getName());
        }
        if (Sender.ROLE_ADMIN.equals(pathParts[USER_PART_ROLE]) && pathParts.length == USER_PARTS_LENGTH) {
            return jwtIsForCorrectSiteAndConsultation(jwtToken, pathParts[USER_PART_SITE], pathParts[USER_PART_CONSULTATION])
                    && jwtHasRoleForTopic(jwtToken, pathParts[USER_PART_MODULE])
                    && pathParts[USER_PART_USER].equals(jwtToken.getName());
        }

        return false;
    }

    private boolean jwtIsForCorrectSiteAndConsultation(JwtAuthenticationToken jwtToken, String site, String consultation) {
        Object payload = jwtToken.getTokenAttributes().get("payload");
        if (!(payload instanceof Map<?, ?> payloadMap)) {
            log.warn("No payload found");
            return false;
        }

        if (!payloadMap.containsKey("site") || payloadMap.get("site") == null || !payloadMap.get("site").equals(site)) {
            log.warn("Incorrect site provided: " + site, payloadMap);
            return false;
        }

        if (!payloadMap.containsKey("consultation") || payloadMap.get("consultation") == null || !payloadMap.get("consultation").equals(consultation)) {
            log.warn("Incorrect consultation provided: " + consultation, payloadMap);
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
