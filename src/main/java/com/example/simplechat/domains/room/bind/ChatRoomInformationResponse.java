package com.example.simplechat.domains.room.bind;

import com.example.simplechat.common.bind.AuditedResponse;
import com.example.simplechat.domains.room.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChatRoomInformationResponse extends AuditedResponse {

	@JsonProperty("id")
	private final long id;

	@JsonProperty("title")
	private final String title;

	private ChatRoomInformationResponse(ChatRoom chatRoom) {
		super(chatRoom.getCreatedAt(), chatRoom.getUpdatedAt());
		this.id = chatRoom.getId();
		this.title = chatRoom.getTitle();
	}

	public static ChatRoomInformationResponse of(ChatRoom chatRoom) {
		return new ChatRoomInformationResponse(chatRoom);
	}
}
