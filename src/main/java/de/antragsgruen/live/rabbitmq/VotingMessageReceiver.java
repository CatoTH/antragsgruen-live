package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.VotingHandler;
import de.antragsgruen.live.metrics.ReceivedRabbitMQMessagesMetric;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQVotingEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class VotingMessageReceiver {
    private static final int RK_PARTS_LENGTH = 4;
    private static final int RK_PART_TOPIC = 0;
    private static final int RK_PART_INSTALLATION = 1;
    private static final int RK_PART_SITE = 2;
    private static final int RK_PART_CONSULTATION = 3;
    private static final String HEADER_DEFAULT_LANGUAGE = "default_language";

    @NonNull private VotingHandler votingHandler;
    @NonNull private ReceivedRabbitMQMessagesMetric receivedRabbitMQMessagesMetric;

    /**
     * @param defaultLanguage the language to deliver to users whose own language the event does not
     *                        contain
     */
    @RabbitListener(queues = {"${antragsgruen.rabbitmq.queue.voting}"})
    public void receiveMessage(
            MQVotingEvent event,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
            @Header(name = HEADER_DEFAULT_LANGUAGE, required = false) String defaultLanguage
    ) {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != RK_PARTS_LENGTH || !"voting".equals(routingKeyParts[RK_PART_TOPIC])) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        // The sections are passed on as they are, but they have to be objects to be merged into one
        // message - a message that is anything else cannot be delivered and is not worth requeueing
        if (event.everyone() == null || !event.everyone().isObject() || event.languages() == null) {
            throw new AmqpRejectAndDontRequeueException("Malformed voting event: " + routingKey);
        }

        ConsultationScope scope = new ConsultationScope(
                routingKeyParts[RK_PART_INSTALLATION],
                routingKeyParts[RK_PART_SITE],
                routingKeyParts[RK_PART_CONSULTATION]
        );

        receivedRabbitMQMessagesMetric.onVotingEvent(scope);
        votingHandler.onVotingEvent(scope, event, defaultLanguage);
    }
}
