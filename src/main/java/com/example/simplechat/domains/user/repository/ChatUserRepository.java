package com.example.simplechat.domains.user.repository;

import com.example.simplechat.domains.user.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;

public interface ChatUserRepository extends CrudRepository<ChatUser, String> {

	default ChatUser findByUsernameOrElseThrow(String username) {
		return findById(username)
			.orElseThrow(() -> new IllegalArgumentException("User not found."));
	}

}
