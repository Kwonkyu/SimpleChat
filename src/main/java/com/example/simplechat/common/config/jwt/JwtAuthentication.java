package com.example.simplechat.common.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JWT principal for JwtAuthenticationToken.
 * It holds raw JWT and username of user.
 */
@Getter
@AllArgsConstructor
public class JwtAuthentication {
	private final String token;
	private final String username;
}
