package de.antragsgruen.live.websocket.dto;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

@Configuration("wsSerializationConfiguration")
@RegisterReflectionForBinding({
        WSGreeting.class,
        WSHelloMessage.class,
        WSSpeechActiveSlot.class,
        WSSpeechQueue.class,
        WSSpeechSubqueue.class,
        WSSpeechSubqueueItem.class,
})
public class SerializationConfiguration {
}
