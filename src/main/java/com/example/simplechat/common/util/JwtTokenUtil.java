package com.example.simplechat.common.util;

import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.common.config.jwt.JwtAuthenticationToken;
import io.jsonwebtoken.*;
import java.util.ArrayList;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final String JWT_SECRET = "JWT_SECRET_!!";

    public String generateJwt(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
            .compact();
    }

    public Claims parseJWTSubject(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtAuthenticationToken validateJwt(String token) {
        Claims claims = parseJWTSubject(token);
        String username = claims.getSubject();
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
            new ArrayList<>(),
            new JwtAuthentication(token, username),
            null);
        jwtAuthenticationToken.setDetails(username); // is it necessary?
        return jwtAuthenticationToken;
    }
}
