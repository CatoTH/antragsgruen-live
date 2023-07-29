package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.rabbitmq.dto.SpeechEvent;
import de.antragsgruen.live.speech.Handler;
import de.antragsgruen.live.websocket.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

public class SpeechMessageReceiver {
    Logger logger = LoggerFactory.getLogger(UserMessageReceiver.class);

    @Autowired
    private Handler speechHandler;

    @RabbitListener(queues = {"${rabbitmq.queue.speech.name}"})
    public void receiveMessage(SpeechEvent event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey)
    {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != 3 || !"speech".equals(routingKeyParts[0])) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        logger.warn("Received user message: " + routingKey + " => " + event.getQueueName());

        speechHandler.onSpeechEvent(routingKeyParts[1], routingKeyParts[2], event);
    }
}
