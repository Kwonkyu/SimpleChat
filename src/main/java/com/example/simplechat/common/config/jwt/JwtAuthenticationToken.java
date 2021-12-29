package com.example.simplechat.common.config.jwt;

import java.util.Collection;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

// reference username password authentication token.
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final transient Object principal;

	// before authenticated.
	public JwtAuthenticationToken(
		String principal // token
	) {
		super(null);
		super.setAuthenticated(false);
		this.principal = principal;
	}

	// after authenticated.
	public JwtAuthenticationToken(
		Collection<? extends GrantedAuthority> authorities,
		Object principal // User or username(current).
	) {
		super(authorities);
		super.setAuthenticated(true);
		this.principal = principal;
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		JwtAuthenticationToken that = (JwtAuthenticationToken) o;
		return principal.equals(that.principal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), principal);
	}
}
