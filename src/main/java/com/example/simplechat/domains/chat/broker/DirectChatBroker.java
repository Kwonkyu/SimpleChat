package com.example.simplechat.domains.chat.broker;

import com.example.simplechat.domains.chat.bind.DirectChatRequest;
import com.example.simplechat.domains.chat.bind.DirectChatResponse;
import com.example.simplechat.domains.chat.service.DirectChatService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DirectChatBroker {

	private final DirectChatService directChatService;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chats/direct/{username}")
	@SendToUser("/chats")
	public DirectChatResponse handleDirectChatRequest(
		Principal principal,
		@DestinationVariable("username") String username,
		@Payload DirectChatRequest request
	) {
		DirectChatResponse chat = directChatService.createChat(
			principal.getName(), username, request.getMessage());
		messagingTemplate.convertAndSendToUser(username, "/chats", chat);
		return chat;
	}

}
