package com.example.simplechat.domains.chat.exception;

import com.example.simplechat.common.exception.ResourceNotFoundException;

public class DirectChatNotFoundException extends ResourceNotFoundException {

	public DirectChatNotFoundException(Long id) {
		super(ResourceType.DIRECT_CHAT.buildNotFoundMessage(id));
	}
}
