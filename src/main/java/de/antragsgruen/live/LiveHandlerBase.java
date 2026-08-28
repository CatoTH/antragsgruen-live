package de.antragsgruen.live;

import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.websocket.TopicPermissionChecker;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class LiveHandlerBase {
    /**
     * One recipient of an event: a user, together with the language they are reading Antragsgrün in
     * (null if their client did not state one).
     * <p>
     * Two browser tabs of the same person reading in two languages are two recipients: they
     * subscribed to two destinations, and each of them is delivered its own wording. Resolving the
     * language per user instead - from their JWT, say - could not do that, as the user registry only
     * knows a user by the connection that registered them first.
     */
    public record Subscriber(String userId, @Nullable String language) {
    }

    protected Collection<Subscriber> findRelevantSubscribers(SimpUserRegistry userRegistry, ConsultationScope scope, String role, String module) {
        // 1) First find all subscriptions with a destination matching
        //    /[role]/[installation]/[site]/[consultation]/[userid]/[module]/[language]
        //    e.g. /user/installation/site/consultation/login-1/speech/de - the language being optional
        // 2) Extract the user ID and the language from the destination
        // 3) Return one entry per user and language (one message per destination is enough)
        Set<Subscriber> subscribers = new LinkedHashSet<>();

        for (SimpSubscription subscription : userRegistry.findSubscriptions(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return TopicPermissionChecker.isUserDestination(parts)
                    && role.equals(parts[TopicPermissionChecker.USER_PART_ROLE])
                    && module.equals(parts[TopicPermissionChecker.USER_PART_MODULE])
                    && scope.installation().equals(parts[TopicPermissionChecker.USER_PART_INSTALLATION])
                    && scope.site().equals(parts[TopicPermissionChecker.USER_PART_SITE])
                    && scope.consultation().equals(parts[TopicPermissionChecker.USER_PART_CONSULTATION]);
        })) {
            String[] parts = subscription.getDestination().split("/");
            subscribers.add(new Subscriber(
                    parts[TopicPermissionChecker.USER_PART_USER],
                    TopicPermissionChecker.languageFromPathParts(parts)
            ));
        }

        return subscribers;
    }
}
