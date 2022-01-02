package com.example.simplechat.domains.room.bind;

import com.example.simplechat.domains.room.entity.ChatRoom;
import com.example.simplechat.domains.room.entity.UserRoomRegistration;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class JoinedUsersResponse {

	@JsonProperty("joinedUsers")
	private final List<JoinedUserResponse> joinedUsers;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	private JoinedUsersResponse(List<JoinedUserResponse> joinedUsers) {
		this.joinedUsers = joinedUsers;
	}

	public static JoinedUsersResponse of(ChatRoom room) {
		return new JoinedUsersResponse(
			room.getUsers()
				.stream()
				.filter(UserRoomRegistration::isJoined)
				.map(JoinedUserResponse::of)
				.collect(Collectors.toList()));
	}
}
