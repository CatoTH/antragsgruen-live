package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.SpeechAdminHandler;
import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.SpeechUserHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class SpeechMessageReceiver {
    private static final int RK_PARTS_LENGTH = 4;
    private static final int RK_PART_TOPIC = 0;
    private static final int RK_PART_INSTALLATION = 1;
    private static final int RK_PART_SITE = 2;
    private static final int RK_PART_CONSULTATION = 3;

    @NonNull private SpeechUserHandler speechUserHandler;
    @NonNull private SpeechAdminHandler speechAdminHandler;

    @RabbitListener(queues = {"${antragsgruen.rabbitmq.queue.speech}"})
    public void receiveMessage(MQSpeechQueue event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != RK_PARTS_LENGTH || !"speech".equals(routingKeyParts[RK_PART_TOPIC])) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        ConsultationScope scope = new ConsultationScope(
                routingKeyParts[RK_PART_INSTALLATION],
                routingKeyParts[RK_PART_SITE],
                routingKeyParts[RK_PART_CONSULTATION]
        );

        speechUserHandler.onSpeechEvent(scope, event);
        speechAdminHandler.onSpeechEvent(scope, event);
    }
}
