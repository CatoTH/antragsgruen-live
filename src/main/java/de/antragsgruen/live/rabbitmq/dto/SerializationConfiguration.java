package de.antragsgruen.live.rabbitmq.dto;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

// Hint: this configuration is needed for GraalVM
@Configuration("rabbitmqSerializationConfiguration")
@RegisterReflectionForBinding({
        MQDebateItem.class,
        MQDebateItemSpeechQueue.class,
        MQDebateItemVotingBlock.class,
        MQDebateState.class,
        MQSpeechQueue.class,
        MQSpeechQueueActiveSlot.class,
        MQSpeechQueueSettings.class,
        MQSpeechSubqueue.class,
        MQSpeechSubqueueItem.class,
})
public class SerializationConfiguration {
}
