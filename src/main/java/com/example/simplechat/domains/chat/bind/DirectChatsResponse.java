package com.example.simplechat.domains.chat.bind;

import com.example.simplechat.domains.chat.entity.DirectChat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class DirectChatsResponse {

	@JsonProperty("chats")
	private final List<DirectChatResponse> chatResponses;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	private DirectChatsResponse(
		@JsonProperty("chats") List<DirectChatResponse> chatResponses
	) {
		this.chatResponses = chatResponses;
	}

	public static DirectChatsResponse of(List<DirectChat> directChats) {
		return new DirectChatsResponse(directChats
										   .stream()
										   .map(DirectChatResponse::of)
										   .collect(Collectors.toList()));
	}
}
