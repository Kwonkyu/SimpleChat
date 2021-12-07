package com.example.simplechat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // enable websocket message handling by message broker.
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// built-in message broker for subscriptions and broadcasting messages whose
		// destination header prefix is ...
		// client may subscribe through this prefix.
		registry.enableSimpleBroker("/topic", "/chats");
		// stomp messages with prefix '/app' routed to controller's message mapping handlers.
		// client send stomp messages to '/app/...' which has '/app' "destination".
		// And then, '...' field will be handled by message mapping annotation.
		registry.setApplicationDestinationPrefixes("/app");
		// destination reserved for single user.
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// websocket handshake endpoint.
		registry.addEndpoint("/websocket-stomp")
				.withSockJS(); // socket js fallback.
	}

}
