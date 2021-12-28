package com.example.simplechat.domains.chat.bind;

import com.example.simplechat.domains.chat.entity.DirectChat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectChatsResponse {

	@JsonProperty("chats")
	private final List<DirectChatResponse> chatResponses;

	public static DirectChatsResponse of(List<DirectChat> directChats) {
		return new DirectChatsResponse(directChats
										   .stream()
										   .map(DirectChatResponse::of)
										   .collect(Collectors.toList()));
	}
}
