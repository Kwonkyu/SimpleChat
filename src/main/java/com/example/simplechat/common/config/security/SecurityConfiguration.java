package com.example.simplechat.common.config.security;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.config.jwt.JwtAuthenticationFilter;
import com.example.simplechat.domains.user.service.ChatUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ChatUserDetailsService chatUserDetailsService;
	private final ObjectMapper objectMapper;

	private void writeJsonOnResponse(
		Object value,
		HttpServletResponse response
	) throws IOException {
		String failJsonResponse = objectMapper.writeValueAsString(value);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(failJsonResponse);
		writer.flush();
	}

		// https://stackoverflow.com/questions/33801468/how-let-spring-security-response-unauthorizedhttp-401-code-if-requesting-uri-w/49966021
	private final AuthenticationEntryPoint unauthorizedEntryPoint = (request, response, authException) -> {
		ApiResponse<Object> fail = ApiResponse.fail(authException.getMessage());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		writeJsonOnResponse(fail, response);
	};

	private final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
		ApiResponse<Object> fail = ApiResponse.fail(accessDeniedException.getMessage());
		response.setStatus(HttpStatus.FORBIDDEN.value());
		writeJsonOnResponse(fail, response);
	};

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring()
		   .antMatchers("/*.html", "/*.js", "/*.css", "/webjars/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.userDetailsService(chatUserDetailsService)
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler)
			.authenticationEntryPoint(unauthorizedEntryPoint); // or HttpStatusEntryPoint

		http
			.csrf()
			.disable()
			.formLogin()
			.disable()
			.cors()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/api/public/**")
			.permitAll()
			.antMatchers(HttpMethod.GET, "/api/room/*")
			.permitAll()
			.anyRequest()
			.authenticated();

		http.addFilterAfter(jwtAuthenticationFilter, SecurityContextPersistenceFilter.class);
	}
}
