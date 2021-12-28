package com.example.simplechat.domains.chat.bind;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DirectChatRequest {

	@NotBlank
	private String message;

}
