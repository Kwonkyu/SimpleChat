package com.example.simplechat.domains.user.service;

import com.example.simplechat.domains.user.bind.ChatUserResponse;
import com.example.simplechat.domains.user.entity.ChatUser;
import com.example.simplechat.domains.user.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class BasicChatUserService {

	private final ChatUserRepository chatUserRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public boolean checkCredentials(String username, String password) {
		return passwordEncoder.matches(
			password,
			chatUserRepository
				.findById(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found."))
				.getHashedPassword()
		);
	}

	public ChatUserResponse createUser(String username, String alias, String password) {
		return ChatUserResponse.of(chatUserRepository.save(
			ChatUser.builder()
					.username(username)
					.alias(alias)
					.hashedPassword(passwordEncoder.encode(password))
					.build()));
	}

	public ChatUserResponse readUser(String username) {
		return ChatUserResponse.of(
			chatUserRepository
				.findById(username)
				.orElseThrow(
					() -> new IllegalArgumentException("User not found.")));
	}

}
