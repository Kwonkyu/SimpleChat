package com.example.simplechat.domains.chat.controller;

import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.domains.chat.bind.DirectChatRequest;
import com.example.simplechat.domains.chat.bind.DirectChatResponse;
import com.example.simplechat.domains.chat.bind.DirectChatsResponse;
import com.example.simplechat.domains.chat.service.DirectChatService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class DirectChatController {

	private final DirectChatService directChatService;

	@GetMapping
	public ResponseEntity<DirectChatsResponse> getChats(
		@AuthenticationPrincipal JwtAuthentication authentication
	) {
		return ResponseEntity.ok(directChatService.getMyChats(authentication.getUsername()));
	}

	@GetMapping("/{chatId}")
	public ResponseEntity<DirectChatResponse> readChat(
		@PathVariable("chatId") long chatId
	) {
		return ResponseEntity.ok(directChatService.getChat(chatId));
	}

	@PostMapping("/{username}")
	public ResponseEntity<DirectChatResponse> sendChat(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("username") String otherUsername,
		@Valid @RequestBody DirectChatRequest request
	) {
		return ResponseEntity.ok(directChatService.sendChat(
			authentication.getUsername(),
			otherUsername,
			request.getMessage()
		));
	}

	@GetMapping("/{username}")
	public ResponseEntity<DirectChatsResponse> getChatsBetween(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("username") String otherUsername
	) {
		return ResponseEntity.ok(
			directChatService.getChatsBetween(
				authentication.getUsername(),
				otherUsername
			));
	}
}
