package de.antragsgruen.live.rabbitmq.dto;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

@Configuration("rabbitmqSerializationConfiguration")
@RegisterReflectionForBinding({
        MQSpeechQueue.class,
        MQSpeechQueueActiveSlot.class,
        MQSpeechQueueSettings.class,
        MQSpeechSubqueue.class,
        MQSpeechSubqueueItem.class,
})
public class SerializationConfiguration {
}
