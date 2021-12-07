package com.example.simplechat;

import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser("member1")
			.password(passwordEncoder().encode("member1"))
			.authorities(new ArrayList<>())
			.and()
			.withUser("member2")
			.password(passwordEncoder().encode("member2"))
			.authorities(new ArrayList<>())
			.and()
			.withUser("center1")
			.password(passwordEncoder().encode("center1"))
			.authorities(new ArrayList<>());
	}

}
