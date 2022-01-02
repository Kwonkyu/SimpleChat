package com.example.simplechat.domains.room.exception;

import com.example.simplechat.common.exception.ResourceNotFoundException;

public class ChatRoomNotFoundException extends ResourceNotFoundException {

	public ChatRoomNotFoundException(Long id) {
		super(ResourceType.CHAT_ROOM.buildNotFoundMessage(id));
	}
}
