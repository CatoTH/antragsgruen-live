package de.antragsgruen.live;

import de.antragsgruen.live.mapper.VotingMapper;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQVotingEvent;
import de.antragsgruen.live.websocket.Sender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Delivers a published voting to those watching it: participants get the section meant for everyone,
 * the administration gets that one plus what only it may see. Which of the two a subscriber is
 * treated as follows from the destination they subscribed to, and subscribing to an /admin/ one
 * requires ROLE_VOTING_ADMIN (see TopicPermissionChecker) - so this server never has to judge
 * anybody's rights here, it only picks the sections Antragsgrün has already separated.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class VotingHandler extends LiveHandlerBase {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    public void onVotingEvent(ConsultationScope scope, MQVotingEvent event, @Nullable String defaultLanguage) {
        this.sendToRole(scope, event, Sender.ROLE_USER, false, defaultLanguage);
        this.sendToRole(scope, event, Sender.ROLE_ADMIN, true, defaultLanguage);
    }

    private void sendToRole(ConsultationScope scope, MQVotingEvent event, String role, boolean isAdmin, @Nullable String defaultLanguage) {
        Collection<Subscriber> subscribers = findRelevantSubscribers(userRegistry, scope, role, Sender.USER_CHANNEL_VOTING);
        if (subscribers.isEmpty()) {
            return;
        }

        log.info("Sending voting " + event.kind() + " event to " + subscribers.size() + " " + role
                + " subscription(s) of " + userRegistry.getUserCount() + " user(s)");

        for (Subscriber subscriber : subscribers) {
            sender.sendToUser(
                    scope,
                    subscriber.userId(),
                    subscriber.language(),
                    role,
                    Sender.USER_CHANNEL_VOTING,
                    VotingMapper.convertForSubscriber(event, isAdmin, subscriber.userId(), subscriber.language(), defaultLanguage)
            );
        }
    }
}
