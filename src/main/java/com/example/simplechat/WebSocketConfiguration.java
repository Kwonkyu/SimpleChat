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
		registry.enableSimpleBroker("/publish");
		// enable simple memory-based broker to send messages back to the client on destination /publish.
		registry.setApplicationDestinationPrefixes("/app");
		// /app prefix for messages that are bound for methods with '@MessageMapping' annotation.
		// i.e. endpoint /app/hello will be handled by GreetingController.greet method(@MessageMapping).
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket-stomp").withSockJS(); // socket js fallback.
	}

}
