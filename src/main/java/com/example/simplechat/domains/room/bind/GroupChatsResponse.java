package com.example.simplechat.domains.room.bind;

import com.example.simplechat.domains.chat.entity.GroupChat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupChatsResponse {

	@JsonProperty("chats")
	private final List<GroupChatResponse> chats;

	public static GroupChatsResponse of(List<GroupChat> groupChats) {
		return new GroupChatsResponse(
			groupChats
				.stream()
				.map(GroupChatResponse::of)
				.collect(Collectors.toList()));
	}
}
