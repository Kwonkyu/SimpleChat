package com.example.simplechat.controller;

import com.example.simplechat.data.GreetingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interrupt")
@RequiredArgsConstructor
public class InterrupterController {

	private final SimpMessagingTemplate messagingTemplate;

	@GetMapping("/{chatId}")
	public GreetingMessage interruptMe(@PathVariable("chatId") String chatId) {
		GreetingMessage greetingMessage = new GreetingMessage("INTERRUPTED BY ANOTHER!");
		messagingTemplate.convertAndSend("/publish/" + chatId, greetingMessage);
		return greetingMessage;
	}

}
