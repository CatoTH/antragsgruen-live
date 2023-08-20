package de.antragsgruen.live;

import org.springframework.messaging.simp.user.SimpUserRegistry;

abstract public class HandlerBase {
    protected String[] findRelevantUserIds(SimpUserRegistry userRegistry, String subdomain, String consultation, String role, String module)
    {
        // 1) First find all subscriptions with a destination matching /[role]/[site]/[consultation]/[userid]/[module]
              // e.g. /user/site/consultation/login-1/speech
        // 2) Extract the User ID from the destinations
        // 3) Return unique User IDs (we only need to send messages to each user once)
        return userRegistry.findSubscriptions(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return parts.length == 6 && role.equals(parts[1]) && module.equals(parts[5])
                    && subdomain.equals(parts[2]) && consultation.equals(parts[3]);
        }).stream().map(subscription -> {
            String[] parts = subscription.getDestination().split("/");
            return parts[4];
        }).distinct().toArray(String[]::new);
    }
}
