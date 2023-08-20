package de.antragsgruen.live.websocket.dto;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

// Hint: this configuration is needed for GraalVM
@Configuration("wsSerializationConfiguration")
@RegisterReflectionForBinding({
        WSGreeting.class,
        WSHelloMessage.class,
        WSSpeechActiveSlot.class,
        WSSpeechQueueAdmin.class,
        WSSpeechQueueSettingsAdmin.class,
        WSSpeechQueueUser.class,
        WSSpeechSubqueueAdmin.class,
        WSSpeechSubqueueAdminItem.class,
        WSSpeechSubqueueUser.class,
        WSSpeechSubqueueUserItem.class,
})
public class SerializationConfiguration {
}
