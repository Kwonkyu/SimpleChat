package com.example.simplechat.common.config.security;

import com.example.simplechat.common.config.jwt.JwtAuthenticationFilter;
import com.example.simplechat.domains.user.service.ChatUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ChatUserDetailsService chatUserDetailsService;

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
		web.ignoring().antMatchers("/*.html", "/*.js", "/*.css", "/webjars/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.userDetailsService(chatUserDetailsService);

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
			.anyRequest()
			.authenticated();

		http.addFilterAfter(jwtAuthenticationFilter, SecurityContextPersistenceFilter.class);
	}
}
