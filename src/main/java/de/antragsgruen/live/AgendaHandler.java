package de.antragsgruen.live;

import de.antragsgruen.live.mapper.AgendaItemMapper;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQAgendaItem;
import de.antragsgruen.live.websocket.Sender;
import de.antragsgruen.live.websocket.dto.WSAgendaItem;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public final class AgendaHandler extends LiveHandlerBase {
    private @NonNull Sender sender;
    private @NonNull SimpUserRegistry userRegistry;

    public void onAgendaEvent(ConsultationScope scope, MQAgendaItem[] mqAgendaItems) {
        String[] users = findRelevantUserIds(userRegistry, scope, Sender.ROLE_USER, Sender.USER_CHANNEL_AGENDA);

        log.info("Sending agenda event to " + users.length + " (out of " + userRegistry.getUserCount() + ") user(s)");

        for (String userId : users) {
            WSAgendaItem[] wsAgendaItems = AgendaItemMapper.convertQueue(mqAgendaItems);

            sender.sendToUser(scope, userId, Sender.ROLE_USER, Sender.USER_CHANNEL_AGENDA, wsAgendaItems);
        }
    }
}
