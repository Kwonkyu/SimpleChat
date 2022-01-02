package com.example.simplechat.domains.room.exception;

import lombok.Getter;

@Getter
public class AlreadyJoinedChatRoomException extends RuntimeException {

	public AlreadyJoinedChatRoomException(String username, long roomId) {
		super(String.format("User %s already joined room #%d", username, roomId));
	}
}
