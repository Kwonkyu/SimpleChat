package com.example.simplechat.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public abstract class ResourceNotFoundException extends RuntimeException {

	protected ResourceNotFoundException(String message) {
		super(message);
	}

	@RequiredArgsConstructor
	protected enum ResourceType {
		CHAT_ROOM("Chat room"),
		CHAT_USER("Chat user"),
		DIRECT_CHAT("Direct chat"),
		GROUP_CHAT("Group chat");

		private final String name;

		public <T> String buildNotFoundMessage(T id) {
			return String.format("%s with id %s not found.", this.name, id);
		}
	}

}
