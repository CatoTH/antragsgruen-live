package de.antragsgruen.live.rabbitmq;

import de.antragsgruen.live.rabbitmq.dto.MQUserEvent;
import de.antragsgruen.live.websocket.Sender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserMessageReceiver {
    private static final int RK_PARTS_LENGTH = 4;
    private static final int RK_PART_SITE = 1;
    private static final int RK_PART_CONSULTATION = 2;
    private static final int RK_PART_USER = 3;

    @NonNull private Sender sender;

    @RabbitListener(queues = {"${antragsgruen.rabbitmq.queue.user}"})
    public void receiveMessage(MQUserEvent event, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        String[] routingKeyParts = routingKey.split("\\.");
        if (routingKeyParts.length != RK_PARTS_LENGTH) {
            throw new AmqpRejectAndDontRequeueException("Invalid routing key: " + routingKey);
        }

        log.warn("Received user message: " + routingKey + " => " + event.username());

        sender.sendToUser(
                routingKeyParts[RK_PART_SITE],
                routingKeyParts[RK_PART_CONSULTATION],
                routingKeyParts[RK_PART_USER],
                Sender.ROLE_USER,
                Sender.USER_CHANNEL_DEFAULT,
                event.username()
        );
    }
}
