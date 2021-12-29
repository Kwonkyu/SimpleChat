package com.example.simplechat.common.exception;

import com.example.simplechat.common.bind.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse<String> handleResourceNotFound(ResourceNotFoundException e) {
		return ApiResponse.fail(e.getMessage());
	}

}
