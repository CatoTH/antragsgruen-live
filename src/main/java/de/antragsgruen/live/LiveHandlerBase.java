package de.antragsgruen.live;

import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.websocket.TopicPermissionChecker;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class LiveHandlerBase {
    private static final String PAYLOAD_CLAIM = "payload";
    private static final String LANGUAGE_CLAIM = "language";

    /**
     * One user an event is to be delivered to, along with the language they are reading Antragsgrün
     * in (null if their client did not state one).
     */
    public record Subscriber(String userId, @Nullable String language) {
    }

    protected Collection<Subscriber> findRelevantSubscribers(SimpUserRegistry userRegistry, ConsultationScope scope, String role, String module) {
        // 1) First find all subscriptions with a destination matching /[role]/[installation]/[site]/[consultation]/[userid]/[module]
              // e.g. /user/installation/site/consultation/login-1/speech
        // 2) Extract the User ID from the destinations and the language from the user's JWT
        // 3) Return one entry per User ID (we only need to send messages to each user once)
        Map<String, Subscriber> subscribers = new LinkedHashMap<>();

        for (SimpSubscription subscription : userRegistry.findSubscriptions(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return parts.length == TopicPermissionChecker.USER_PARTS_LENGTH
                    && role.equals(parts[TopicPermissionChecker.USER_PART_ROLE])
                    && module.equals(parts[TopicPermissionChecker.USER_PART_MODULE])
                    && scope.installation().equals(parts[TopicPermissionChecker.USER_PART_INSTALLATION])
                    && scope.site().equals(parts[TopicPermissionChecker.USER_PART_SITE])
                    && scope.consultation().equals(parts[TopicPermissionChecker.USER_PART_CONSULTATION]);
        })) {
            String userId = subscription.getDestination().split("/")[TopicPermissionChecker.USER_PART_USER];
            subscribers.computeIfAbsent(userId, id -> new Subscriber(id, getLanguageOfSubscription(subscription)));
        }

        return subscribers.values();
    }

    /**
     * The language a user is reading Antragsgrün in, as stated by the JWT they connected with.
     * Not used for authorization - it only selects between the languages an event was sent in.
     * <p>
     * Hint: this is the language of the connection the user registry knows the user by. A user
     * reading the same consultation in two languages at once (two browser tabs) therefore gets one
     * of them in both, as the destination messages are sent to is addressed by user, not by session.
     */
    private static @Nullable String getLanguageOfSubscription(SimpSubscription subscription) {
        SimpUser user = subscription.getSession().getUser();
        Principal principal = (user != null ? user.getPrincipal() : null);

        if (!(principal instanceof JwtAuthenticationToken jwtToken)) {
            return null;
        }
        if (!(jwtToken.getTokenAttributes().get(PAYLOAD_CLAIM) instanceof Map<?, ?> payload)) {
            return null;
        }

        return (payload.get(LANGUAGE_CLAIM) instanceof String language && !language.isEmpty() ? language : null);
    }
}
