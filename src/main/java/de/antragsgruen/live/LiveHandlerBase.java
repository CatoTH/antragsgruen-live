package de.antragsgruen.live;

import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.websocket.TopicPermissionChecker;
import org.springframework.messaging.simp.user.SimpUserRegistry;

public abstract class LiveHandlerBase {
    protected String[] findRelevantUserIds(SimpUserRegistry userRegistry, ConsultationScope scope, String role, String module) {
        // 1) First find all subscriptions with a destination matching /[role]/[installation]/[site]/[consultation]/[userid]/[module]
              // e.g. /user/installation/site/consultation/login-1/speech
        // 2) Extract the User ID from the destinations
        // 3) Return unique User IDs (we only need to send messages to each user once)
        return userRegistry.findSubscriptions(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return parts.length == TopicPermissionChecker.USER_PARTS_LENGTH
                    && role.equals(parts[TopicPermissionChecker.USER_PART_ROLE])
                    && module.equals(parts[TopicPermissionChecker.USER_PART_MODULE])
                    && scope.installation().equals(parts[TopicPermissionChecker.USER_PART_INSTALLATION])
                    && scope.site().equals(parts[TopicPermissionChecker.USER_PART_SITE])
                    && scope.consultation().equals(parts[TopicPermissionChecker.USER_PART_CONSULTATION]);
        }).stream().map(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return parts[TopicPermissionChecker.USER_PART_USER];
        }).distinct().toArray(String[]::new);
    }
}
