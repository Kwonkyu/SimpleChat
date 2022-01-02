package com.example.simplechat.common.config.jwt;

import com.example.simplechat.common.util.JwtTokenUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenUtil jwtTokenUtil;

	@Override
	public void doFilter(
		ServletRequest request, ServletResponse response, FilterChain chain
	) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String jwt = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if(jwt != null && jwt.startsWith("Bearer ")) {
			JwtAuthenticationToken jwtAuthenticationToken = jwtTokenUtil.validateJwt(jwt.substring(7));
			SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
		}
		chain.doFilter(request, response);
	}
}