package de.antragsgruen.live.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueUserConfiguration {
    @Value("${rabbitmq.queue.user}")
    private String queueNameUser;

    @Value("${rabbitmq.queue.user_dead}")
    private String queueNameUserDead;

    @Value("${rabbitmq.routing.user}")
    private String userRoutingKey;

    @Value("${rabbitmq.exchange.name_dead}")
    private String exchangeNameDead;

    @Bean
    Queue queueUser() {
        return QueueBuilder
                .durable(queueNameUser)
                .deadLetterExchange(exchangeNameDead)
                .build();
    }

    @Bean
    Binding userBinding(@Qualifier("queueUser") Queue queue, @Qualifier("exchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(userRoutingKey);
    }

    @Bean
    Queue queueUserDead() {
        return new Queue(queueNameUserDead, true);
    }

    @Bean
    Binding userDeadBinding(@Qualifier("queueUserDead") Queue queue, @Qualifier("deadLetterExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(userRoutingKey);
    }
}
