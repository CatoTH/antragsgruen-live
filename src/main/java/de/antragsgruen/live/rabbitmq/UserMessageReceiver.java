package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.rabbitmq.dto.MQUserEvent;
import de.antragsgruen.live.websocket.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class UserMessageReceiver {
    Logger logger = LoggerFactory.getLogger(UserMessageReceiver.class);

    @Autowired
    private Sender sender;

    @RabbitListener(queues = {"${rabbitmq.queue.user.name}"})
    public void receiveMessage(MQUserEvent event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey)
    {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != 4) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        logger.warn("Received user message: " + routingKey + " => " + event.getUsername());

        sender.sendToUser(routingKeyParts[1], routingKeyParts[2], routingKeyParts[3], Sender.USER_CHANNEL_DEFAULT, event.getUsername());
    }
}
