package com.example.simplechat.domains.chat.controller;

import com.example.simplechat.controller.bind.ChatNotification;
import com.example.simplechat.controller.bind.ChatRequest;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	@Autowired
	SimpMessagingTemplate template;

//	@SubscribeMapping("/chats") // maybe it's for broadcast?
	@MessageMapping("/chats/send/{username}")
	@SendToUser("/chats")
	public ChatNotification sendChat(
		@DestinationVariable("username") String receiver,
		@Payload ChatRequest chatRequest,
		Principal principal
	) {
		template.convertAndSendToUser(
			receiver,
			"/chats",
			new ChatNotification(principal.getName(), chatRequest.getContent()));
		return new ChatNotification(principal.getName(), chatRequest.getContent());
	}

}
