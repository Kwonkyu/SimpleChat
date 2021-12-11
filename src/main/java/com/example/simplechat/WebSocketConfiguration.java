package com.example.simplechat;

import com.example.simplechat.security.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // enable websocket message handling by message broker.
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	private final JwtTokenUtil jwtTokenUtil;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// built-in message broker for subscriptions and broadcasting messages whose
		// destination header prefix is ...
		// client may subscribe through this prefix.
		registry.enableSimpleBroker("/posts", "/chats");
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

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(
				Message<?> message, MessageChannel channel
			) {
				StompHeaderAccessor accessor =
					MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
					String jwt = String.valueOf(accessor.getFirstNativeHeader("Authorization"));
					JwtAuthenticationToken jwtAuthenticationToken = jwtTokenUtil.validateJwt(jwt);
					accessor.setUser(jwtAuthenticationToken);
				}
				return message;
			}
		});
	}
}
