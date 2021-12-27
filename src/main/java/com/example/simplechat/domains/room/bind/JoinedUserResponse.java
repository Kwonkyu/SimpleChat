package com.example.simplechat.domains.room.bind;

import com.example.simplechat.domains.room.entity.UserRoomRegistration;
import com.example.simplechat.domains.user.entity.ChatUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class JoinedUserResponse {

	@JsonProperty("username")
	private final String username;

	@JsonProperty("alias")
	private final String alias;

	@JsonProperty("joinedAt")
	private final LocalDateTime joinedAt;

	private JoinedUserResponse(UserRoomRegistration registration) {
		ChatUser user = registration.getUser();
		this.username = user.getUsername();
		this.alias = user.getAlias();
		this.joinedAt = registration.getUpdatedAt() == null ?
			registration.getCreatedAt() :
			registration.getUpdatedAt();
	}

	public static JoinedUserResponse of(UserRoomRegistration registration) {
		return new JoinedUserResponse(registration);
	}
}
