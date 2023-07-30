package de.antragsgruen.live.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenerConfiguration {
    @Value("${rabbitmq.exchange.name}")
    private String topicExchangeName;

    @Value("${rabbitmq.queue.user.name}")
    private String userQueueName;

    @Value("${rabbitmq.routing.user.key}")
    private String userRoutingKey;

    @Value("${rabbitmq.queue.speech.name}")
    private String speechQueueName;

    @Value("${rabbitmq.routing.speech.key}")
    private String speechRoutingKey;

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Queue userQueue() {
        return new Queue(userQueueName, false);
    }

    @Bean
    Binding userBinding(@Qualifier("userQueue") Queue queue, TopicExchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with(userRoutingKey);
    }

    @Bean
    Queue speechQueue() {
        return new Queue(speechQueueName, false);
    }

    @Bean
    Binding speechBinding(@Qualifier("speechQueue") Queue queue, TopicExchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with(speechRoutingKey);
    }

    // @TODO: Set up a dead-letter queue (https://www.baeldung.com/spring-amqp-error-handling)

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
}
