package com.example.simplechat.domains.room.exception;

import com.example.simplechat.common.bind.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChatRoomControllerAdvice {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<Object> handleAlreadyJoinedChatRoomException(AlreadyJoinedChatRoomException e) {
		return ApiResponse.fail(e.getMessage());
	}

}
