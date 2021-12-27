package com.example.simplechat.domains.user.bind;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChatUserInformationRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String alias;

	@NotBlank
	private String password;

}
