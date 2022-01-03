package com.example.simplechat.domains.chat.controller;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.domains.chat.bind.DirectChatRequest;
import com.example.simplechat.domains.chat.bind.DirectChatResponse;
import com.example.simplechat.domains.chat.bind.DirectChatsResponse;
import com.example.simplechat.domains.chat.service.DirectChatService;
import java.net.URI;
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
	public ResponseEntity<ApiResponse<DirectChatsResponse>> getChats(
		@AuthenticationPrincipal JwtAuthentication authentication
	) {
		return ResponseEntity.ok(
			ApiResponse.success(
				directChatService.readMyChats(authentication.getUsername())));
	}

	@GetMapping("/{username}")
	public ResponseEntity<ApiResponse<DirectChatsResponse>> getChatsBetween(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("username") String otherUsername
	) {
		return ResponseEntity.ok(
			ApiResponse.success(
				directChatService.readChatsBetween(
					authentication.getUsername(),
					otherUsername
				)));
	}

	@PostMapping("/{username}")
	public ResponseEntity<ApiResponse<DirectChatResponse>> sendChat(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("username") String otherUsername,
		@Valid @RequestBody DirectChatRequest request
	) {
		DirectChatResponse chat = directChatService.createChat(
			authentication.getUsername(),
			otherUsername,
			request.getMessage()
		);
		return ResponseEntity
			.created(URI.create("/api/chat/" + chat.getId()))
			.body(ApiResponse.success(chat));
	}

}
