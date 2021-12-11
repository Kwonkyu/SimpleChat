package com.example.simplechat.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

// reference username password authentication token.
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;
	private String credentials;

	// before authenticated.
	public JwtAuthenticationToken(
		String principal, // token
		String credentials // null
	) {
		super(null);
		super.setAuthenticated(false);
		this.principal = principal;
		this.credentials = credentials;
	}

	// after authenticated.
	public JwtAuthenticationToken(
		Collection<? extends GrantedAuthority> authorities,
		Object principal, // User or username(current).
		String credentials // null
	) {
		super(authorities);
		super.setAuthenticated(true);
		this.principal = principal;
		this.credentials = credentials;
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public void clearCredentials() {
		this.credentials = null;
	}
}
