package de.antragsgruen.live.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueDebateConfiguration {
    @Value("${antragsgruen.rabbitmq.queue.debate}")
    private String queueNameDebate;

    @Value("${antragsgruen.rabbitmq.queue.debate_dead}")
    private String queueNameDebateDead;

    @Value("${antragsgruen.rabbitmq.routing.debate}")
    private String debateRoutingKey;

    @Value("${antragsgruen.rabbitmq.exchange.name_dead}")
    private String exchangeNameDead;

    @Bean
    Queue debate() {
        return QueueBuilder
                .durable(queueNameDebate)
                .deadLetterExchange(exchangeNameDead)
                .build();
    }

    @Bean
    Binding debateBinding(@Qualifier("debate") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(debateRoutingKey);
    }

    @Bean
    Queue queueDebateDead() {
        return new Queue(queueNameDebateDead, true);
    }

    @Bean
    Binding debateDeadBinding(@Qualifier("queueDebateDead") Queue queue, @Qualifier("deadLetterExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(debateRoutingKey);
    }
}
