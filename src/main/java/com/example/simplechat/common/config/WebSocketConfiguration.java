package com.example.simplechat.common.config;

import com.example.simplechat.common.util.JwtTokenUtil;
import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.common.config.jwt.JwtAuthenticationToken;
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
		registry.enableSimpleBroker("/chats/direct", "/chats/group", "/chats");
		registry.setApplicationDestinationPrefixes("/app");
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// websocket handshake endpoint.
		registry.addEndpoint("/websocket-stomp")
				.setAllowedOriginPatterns("*")
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
					accessor.setUser(((JwtAuthentication) jwtAuthenticationToken.getPrincipal())::getUsername);
				}
				return message;
			}
		});
	}
}
