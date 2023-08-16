package de.antragsgruen.live.rabbitmq;

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
    @NonNull private SpeechUserHandler speechUserHandler;

    @RabbitListener(queues = {"${rabbitmq.queue.speech.name}"})
    public void receiveMessage(MQSpeechQueue event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey)
    {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != 3 || !"speech".equals(routingKeyParts[0])) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        speechUserHandler.onSpeechEvent(routingKeyParts[1], routingKeyParts[2], event);
    }
}
