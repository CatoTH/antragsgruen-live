package de.antragsgruen.live.rabbitmq.configuration;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagesConfiguration {
    @Bean
    public MessageConverter converter() {
        return new JacksonJsonMessageConverter();
    }
}
