package com.example.simplechat.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAuthentication {
	private final String token;
	private final String username;
}
