package com.example.simplechat.domains.chat.bind;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DirectChatRequest {

	@NotBlank
	private String message;

}
