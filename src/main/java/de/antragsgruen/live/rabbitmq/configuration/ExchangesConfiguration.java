package de.antragsgruen.live.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangesConfiguration {
    @Value("${antragsgruen.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${antragsgruen.rabbitmq.exchange.name_dead}")
    private String exchangeNameDead;

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(exchangeNameDead);
    }
}
