package com.example.simplechat.domains.user.controller;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.domains.user.bind.ChatUserResponse;
import com.example.simplechat.domains.user.service.BasicChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ChatUserController {

	private final BasicChatUserService chatUserService;

	@GetMapping
	public ResponseEntity<ApiResponse<ChatUserResponse>> readUser(
		@AuthenticationPrincipal JwtAuthentication authentication
	) {
		return ResponseEntity.ok(
			ApiResponse.success(chatUserService.readUser(authentication.getUsername())));
	}

}
