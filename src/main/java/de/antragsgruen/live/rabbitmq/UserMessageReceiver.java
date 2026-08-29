package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.LiveHandlerBase;
import de.antragsgruen.live.metrics.ReceivedRabbitMQMessagesMetric;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQUserEvent;
import de.antragsgruen.live.websocket.Sender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public final class UserMessageReceiver extends LiveHandlerBase {
    private static final int RK_PARTS_LENGTH = 5;
    private static final int RK_PART_INSTALLATION = 1;
    private static final int RK_PART_SITE = 2;
    private static final int RK_PART_CONSULTATION = 3;
    private static final int RK_PART_USER = 4;

    @NonNull private Sender sender;
    @NonNull private SimpUserRegistry userRegistry;
    @NonNull private ReceivedRabbitMQMessagesMetric receivedRabbitMQMessagesMetric;

    @RabbitListener(queues = {"${antragsgruen.rabbitmq.queue.user}"})
    public void receiveMessage(MQUserEvent event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != RK_PARTS_LENGTH) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        ConsultationScope scope = new ConsultationScope(
                routingKeyParts[RK_PART_INSTALLATION],
                routingKeyParts[RK_PART_SITE],
                routingKeyParts[RK_PART_CONSULTATION]
        );

        log.debug("Received user message: " + routingKey + " => " + event.username());

        receivedRabbitMQMessagesMetric.onUserEvent(scope);

        // Addressed at one person, but delivered per subscription: someone reading in two languages
        // has two destinations, and the message has to reach both of them.
        String userId = routingKeyParts[RK_PART_USER];
        findRelevantSubscribers(userRegistry, scope, Sender.ROLE_USER, Sender.USER_CHANNEL_DEFAULT)
                .stream()
                .filter(subscriber -> subscriber.userId().equals(userId))
                .forEach(subscriber -> sender.sendToUser(
                        scope, subscriber.userId(), subscriber.language(),
                        Sender.ROLE_USER, Sender.USER_CHANNEL_DEFAULT, event.username()
                ));
    }
}
