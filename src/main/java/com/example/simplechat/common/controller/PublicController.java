package com.example.simplechat.common.controller;

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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Transactional
public class PublicController {

	private final BasicChatUserService chatUserService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;

	@PostMapping("/login")
	@Transactional(readOnly = true)
	public ResponseEntity<String> login(
		@Valid @RequestBody LoginRequest request
	) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
			request.getUsername(), request.getPassword());
		Authentication authenticate = authenticationManager.authenticate(token);
		if (!authenticate.isAuthenticated()) {
			throw new BadCredentialsException("Password not matched.");
		}

		return ResponseEntity.ok(jwtTokenUtil.generateJwt(authenticate.getName()));
	}


	@PostMapping("/register")
	public ResponseEntity<ChatUserResponse> createUser(
		@Valid @RequestBody ChatUserInformationRequest request
	) {
		return ResponseEntity
			.created(URI.create("/location"))
			.body(chatUserService.createUser(
				request.getUsername(),
				request.getAlias(),
				request.getPassword()
			));
	}

}
