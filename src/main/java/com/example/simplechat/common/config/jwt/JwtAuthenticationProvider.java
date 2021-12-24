package com.example.simplechat.common.config.jwt;

import com.example.simplechat.common.util.JwtTokenUtil;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtTokenUtil jwtTokenUtil;

	@Override
	public Authentication authenticate(Authentication authentication)
		throws AuthenticationException {
		String username = String.valueOf(authentication.getPrincipal());
		return new JwtAuthenticationToken(
			new ArrayList<>(),
			new JwtAuthentication(jwtTokenUtil.generateJwt(username), username),
			null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
