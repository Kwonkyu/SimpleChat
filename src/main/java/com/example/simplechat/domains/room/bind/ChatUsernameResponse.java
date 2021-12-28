package com.example.simplechat.domains.room.bind;

import com.example.simplechat.domains.user.entity.ChatUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChatUsernameResponse {

	@JsonProperty("username")
	private final String username;

	@JsonProperty("alias")
	private final String alias;

	private ChatUsernameResponse(ChatUser chatUser) {
		this.username = chatUser.getUsername();
		this.alias = chatUser.getAlias();
	}

	public static ChatUsernameResponse of(ChatUser chatUser) {
		return new ChatUsernameResponse(chatUser);
	}
}
