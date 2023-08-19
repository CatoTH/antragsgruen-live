package de.antragsgruen.live.websocket;

import de.antragsgruen.live.websocket.dto.*;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RegisterReflectionForBinding({
        WSGreeting.class,
        WSHelloMessage.class,
        WSSpeechActiveSlot.class,
        WSSpeechQueue.class,
        WSSpeechSubqueue.class,
        WSSpeechSubqueueItem.class,
})
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Value("${antragsgruen.ws.origins}")
    private String wsOriginPatter;

    @Autowired
    private WebsocketChannelInterceptor interceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        config.enableSimpleBroker("/user", "/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry.addEndpoint("/websocket").setAllowedOriginPatterns(this.wsOriginPatter);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration)
    {
        registration.interceptors(this.interceptor);
    }
}
