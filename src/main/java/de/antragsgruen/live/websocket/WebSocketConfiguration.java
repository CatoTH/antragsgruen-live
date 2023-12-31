package de.antragsgruen.live.websocket;

import de.antragsgruen.live.websocket.dto.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RegisterReflectionForBinding({
        WSGreeting.class,
        WSHelloMessage.class,
        WSSpeechActiveSlot.class,
        WSSpeechQueueUser.class,
        WSSpeechSubqueueUser.class,
        WSSpeechSubqueueUserItem.class,
})
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Value("${antragsgruen.ws.heartbeat}")
    private long wsHeartbeatInterval;

    @Value("${antragsgruen.ws.origins}")
    private String wsOriginPatterns;

    @Autowired
    private WebsocketChannelInterceptor interceptor;

    private TaskScheduler messageBrokerTaskScheduler;

    @Autowired
    public void setMessageBrokerTaskScheduler(@Lazy TaskScheduler taskScheduler) {
        this.messageBrokerTaskScheduler = taskScheduler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config
                .enableSimpleBroker("/user", "/admin", "/topic", "/queue")
                .setHeartbeatValue(new long[]{wsHeartbeatInterval, wsHeartbeatInterval})
                .setTaskScheduler(messageBrokerTaskScheduler);
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] patterns = this.wsOriginPatterns.split(",");
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns(patterns);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(this.interceptor);
    }
}
