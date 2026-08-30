package de.antragsgruen.live.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueVotingConfiguration {
    @Value("${antragsgruen.rabbitmq.queue.voting}")
    private String queueNameVoting;

    @Value("${antragsgruen.rabbitmq.queue.voting_dead}")
    private String queueNameVotingDead;

    @Value("${antragsgruen.rabbitmq.routing.voting}")
    private String votingRoutingKey;

    @Value("${antragsgruen.rabbitmq.exchange.name_dead}")
    private String exchangeNameDead;

    @Bean
    Queue voting() {
        return QueueBuilder
                .durable(queueNameVoting)
                .deadLetterExchange(exchangeNameDead)
                .build();
    }

    @Bean
    Binding votingBinding(@Qualifier("voting") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(votingRoutingKey);
    }

    @Bean
    Queue queueVotingDead() {
        return new Queue(queueNameVotingDead, true);
    }

    @Bean
    Binding votingDeadBinding(@Qualifier("queueVotingDead") Queue queue, @Qualifier("deadLetterExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(votingRoutingKey);
    }
}
