package com.example.simplechat.controller;

import com.example.simplechat.JwtTokenUtil;
import com.example.simplechat.controller.bind.LoginRequest;
import com.example.simplechat.security.JwtAuthentication;
import com.example.simplechat.security.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;

	@PostMapping
	public ResponseEntity<String> login(@RequestBody LoginRequest request) {
		JwtAuthenticationToken token = new JwtAuthenticationToken(
			request.getUsername(), request.getPassword());
		Authentication authenticate = authenticationManager.authenticate(token);
		return ResponseEntity.ok(jwtTokenUtil.generateJwt(
			((JwtAuthentication) authenticate.getPrincipal()).getUsername()));
	}

}
