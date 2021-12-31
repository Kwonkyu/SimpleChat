package com.example.simplechat.domains.room.bind;

import com.example.simplechat.domains.room.entity.UserRoomRegistration;
import com.example.simplechat.domains.user.entity.ChatUser;
import com.fasterxml.jackson.annotation.JsonCreator;
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

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	private JoinedUserResponse(
		@JsonProperty("username") String username,
		@JsonProperty("alias") String alias,
		@JsonProperty("joinedAt") LocalDateTime joinedAt) {
		this.username = username;
		this.alias = alias;
		this.joinedAt = joinedAt;
	}

	public static JoinedUserResponse of(UserRoomRegistration registration) {
		ChatUser user = registration.getUser();
		return new JoinedUserResponse(
			user.getUsername(),
			user.getAlias(),
			registration.getUpdatedAt());
	}
}
