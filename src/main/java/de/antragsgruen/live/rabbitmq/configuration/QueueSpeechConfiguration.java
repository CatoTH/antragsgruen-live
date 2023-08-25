package de.antragsgruen.live.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueSpeechConfiguration {
    @Value("${antragsgruen.rabbitmq.queue.speech}")
    private String queueNameSpeech;

    @Value("${antragsgruen.rabbitmq.queue.speech_dead}")
    private String queueNameSpeechDead;

    @Value("${antragsgruen.rabbitmq.routing.speech}")
    private String speechRoutingKey;

    @Value("${antragsgruen.rabbitmq.exchange.name_dead}")
    private String exchangeNameDead;

    @Bean
    Queue speechQueue() {
        return QueueBuilder
                .durable(queueNameSpeech)
                .deadLetterExchange(exchangeNameDead)
                .build();
    }

    @Bean
    Binding speechBinding(@Qualifier("speechQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(speechRoutingKey);
    }

    @Bean
    Queue queueSpeechDead() {
        return new Queue(queueNameSpeechDead, true);
    }

    @Bean
    Binding speechDeadBinding(@Qualifier("queueSpeechDead") Queue queue, @Qualifier("deadLetterExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(speechRoutingKey);
    }
}
