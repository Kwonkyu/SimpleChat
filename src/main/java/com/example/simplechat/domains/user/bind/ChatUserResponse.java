package com.example.simplechat.domains.user.bind;

import com.example.simplechat.domains.room.bind.ChatRoomInformationResponse;
import com.example.simplechat.domains.room.entity.UserRoomId;
import com.example.simplechat.domains.room.entity.UserRoomRegistration;
import com.example.simplechat.domains.user.entity.ChatUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ChatUserResponse {

	@JsonProperty("username")
	private final String username;

	@JsonProperty("alias")
	private final String alias;

	@JsonProperty("joinedRooms")
	private final List<ChatRoomInformationResponse> rooms;

	private ChatUserResponse(ChatUser chatUser) {
		this.username = chatUser.getUsername();
		this.alias = chatUser.getAlias();
		this.rooms = chatUser.getRooms()
							 .stream()
							 .map(UserRoomRegistration::getId)
							 .map(UserRoomId::getChatRoom)
							 .map(ChatRoomInformationResponse::of)
							 .collect(Collectors.toList());
	}

	public static ChatUserResponse of(ChatUser chatUser) {
		return new ChatUserResponse(chatUser);
	}
}
