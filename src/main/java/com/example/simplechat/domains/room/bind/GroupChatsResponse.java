package com.example.simplechat.domains.room.bind;

import com.example.simplechat.domains.room.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class GroupChatsResponse {

	@JsonProperty("chats")
	private final List<GroupChatResponse> chats;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	private GroupChatsResponse(@JsonProperty("chats") List<GroupChatResponse> chats) {
		this.chats = chats;
	}

	public static GroupChatsResponse of(ChatRoom chatRoom) {
		return new GroupChatsResponse(
			chatRoom
				.getGroupChats()
				.stream()
				.map(GroupChatResponse::of)
				.collect(Collectors.toList()));
	}
}
