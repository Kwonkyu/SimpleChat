package com.example.simplechat.common.util;

import com.example.simplechat.common.bind.JwtResponse;
import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.common.config.jwt.JwtAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	private static final String JWT_SECRET = "JWT_SECRET_!!";

	private JwtBuilder buildUntilExpirationDate(String username) {
		return Jwts.builder()
				   .setSubject(username)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .signWith(SignatureAlgorithm.HS256, JWT_SECRET);
	}

	public String generateAccessToken(String username) {
		return buildUntilExpirationDate(username)
			.setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
			.compact();
	}

	public String generateRefreshToken(String username) {
		return buildUntilExpirationDate(username)
            .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
			.compact();
	}

	public JwtResponse generateJwt(String username) {
		return new JwtResponse(generateAccessToken(username), generateRefreshToken(username));
	}

	public Claims parseJwtClaims(String token) {
		return Jwts.parser()
				   .setSigningKey(JWT_SECRET)
				   .parseClaimsJws(token)
				   .getBody();
	}

	public JwtAuthenticationToken validateJwt(String token) {
		Claims claims = parseJwtClaims(token);
		String username = claims.getSubject();
		JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
			new ArrayList<>(),
			new JwtAuthentication(token, username)
		);
		jwtAuthenticationToken.setDetails(username); // is it necessary?
		return jwtAuthenticationToken;
	}
}
