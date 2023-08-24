package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.SpeechAdminHandler;
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
public class SpeechMessageReceiver {
    private static final int RK_PARTS_LENGTH = 3;
    private static final int RK_PARTS_TOPIC = 0;
    private static final int RK_PARTS_SITE = 1;
    private static final int RK_PARTS_CONSULTATION = 2;

    @NonNull private SpeechUserHandler speechUserHandler;
    @NonNull private SpeechAdminHandler speechAdminHandler;

    @RabbitListener(queues = {"${rabbitmq.queue.speech.name}"})
    public void receiveMessage(MQSpeechQueue event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != RK_PARTS_LENGTH || !"speech".equals(routingKeyParts[RK_PARTS_TOPIC])) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        speechUserHandler.onSpeechEvent(routingKeyParts[RK_PARTS_SITE], routingKeyParts[RK_PARTS_CONSULTATION], event);
        speechAdminHandler.onSpeechEvent(routingKeyParts[RK_PARTS_SITE], routingKeyParts[RK_PARTS_CONSULTATION], event);
    }
}
