package com.example.simplechat.domains.user.exception;

import com.example.simplechat.common.exception.ResourceNotFoundException;

public class ChatUserNotFoundException extends ResourceNotFoundException {

	public ChatUserNotFoundException(String username) {
		super(ResourceType.CHAT_USER.buildNotFoundMessage(username));
	}
}
