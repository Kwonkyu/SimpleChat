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
		// subscription url prefix. i.e. '@SendTo' annotation.
		registry.enableSimpleBroker("/notice");
		// prefix for client's send endpoint. client send data to '/notice/...'
		// and it'll be handled by message brokers.
		registry.setApplicationDestinationPrefixes("/publish");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket-stomp").withSockJS(); // socket js fallback.
	}

}
