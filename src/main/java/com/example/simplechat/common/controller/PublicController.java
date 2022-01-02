package com.example.simplechat.common.controller;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.bind.JwtResponse;
import com.example.simplechat.common.bind.LoginRequest;
import com.example.simplechat.common.util.JwtTokenUtil;
import com.example.simplechat.domains.user.bind.ChatUserInformationRequest;
import com.example.simplechat.domains.user.bind.ChatUserResponse;
import com.example.simplechat.domains.user.service.BasicChatUserService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

	private final BasicChatUserService chatUserService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<JwtResponse>> login(
		@Valid @RequestBody LoginRequest request
	) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
			request.getUsername(), request.getPassword());
		Authentication authenticate = authenticationManager.authenticate(token);
		if (!authenticate.isAuthenticated()) {
			throw new AuthenticationException("Authentication failed for unknown reason.") {
			};
		}

		return ResponseEntity
			.ok(ApiResponse.success(
				jwtTokenUtil.generateJwt(authenticate.getName())));
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<ChatUserResponse>> createUser(
		@Valid @RequestBody ChatUserInformationRequest request
	) {
		return ResponseEntity
			.created(URI.create("/api/user"))
			.body(ApiResponse.success(
				chatUserService.createUser(
					request.getUsername(),
					request.getAlias(),
					request.getPassword()
				)));
	}

}
