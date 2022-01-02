package com.example.simplechat.domains.user.service;

import com.example.simplechat.domains.user.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatUserDetailsService implements UserDetailsService {

	private final ChatUserRepository chatUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return chatUserRepository
			.findById(username)
			.orElseThrow(
				() -> new IllegalArgumentException("User not found."));
	}
}
