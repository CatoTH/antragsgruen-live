package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.rabbitmq.dto.MQSpeechQueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueue;
import de.antragsgruen.live.rabbitmq.dto.MQSpeechSubqueueItem;
import de.antragsgruen.live.speech.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class SpeechMessageReceiver {
    Logger logger = LoggerFactory.getLogger(UserMessageReceiver.class);

    @Autowired
    private Handler speechHandler;

    @RabbitListener(queues = {"${rabbitmq.queue.speech.name}"})
    public void receiveMessage(MQSpeechQueue event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey)
    {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != 3 || !"speech".equals(routingKeyParts[0])) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        logger.warn("Received speech message: " + routingKey + " => " + event.getId());
        for (MQSpeechSubqueue sub : event.getSubqueues()) {
            logger.warn("Subqueue: " + (sub.getName() == null ? "-null-" : sub.getName()));
            for (MQSpeechSubqueueItem item : sub.getItems()) {
                logger.warn("Subqueue user: " + item.getName());
            }
        }

        speechHandler.onSpeechEvent(routingKeyParts[1], routingKeyParts[2], event);
    }
}
