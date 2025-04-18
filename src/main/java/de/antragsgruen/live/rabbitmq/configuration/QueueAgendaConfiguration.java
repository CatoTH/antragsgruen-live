package de.antragsgruen.live.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueAgendaConfiguration {
    @Value("${antragsgruen.rabbitmq.queue.agenda}")
    private String queueNameAgenda;

    @Value("${antragsgruen.rabbitmq.queue.agenda_dead}")
    private String queueNameAgendaDead;

    @Value("${antragsgruen.rabbitmq.routing.agenda}")
    private String agendaRoutingKey;

    @Value("${antragsgruen.rabbitmq.exchange.name_dead}")
    private String exchangeNameDead;

    @Bean
    Queue agendaQueue() {
        return QueueBuilder
                .durable(queueNameAgenda)
                .deadLetterExchange(exchangeNameDead)
                .build();
    }

    @Bean
    Binding agendaBinding(@Qualifier("agendaQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(agendaRoutingKey);
    }

    @Bean
    Queue queueAgendaDead() {
        return new Queue(queueNameAgendaDead, true);
    }

    @Bean
    Binding agendahDeadBinding(@Qualifier("queueAgendaDead") Queue queue, @Qualifier("deadLetterExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(agendaRoutingKey);
    }
}
