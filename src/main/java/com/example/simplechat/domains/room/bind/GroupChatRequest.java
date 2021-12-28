package com.example.simplechat.domains.room.bind;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GroupChatRequest {

	@NotBlank
	private String message;

}
