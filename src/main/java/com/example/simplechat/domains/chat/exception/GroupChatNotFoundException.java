package com.example.simplechat.domains.chat.exception;

import com.example.simplechat.common.exception.ResourceNotFoundException;

public class GroupChatNotFoundException extends ResourceNotFoundException {

	public GroupChatNotFoundException(Long id) {
		super(ResourceType.GROUP_CHAT.buildNotFoundMessage(id));
	}
}
