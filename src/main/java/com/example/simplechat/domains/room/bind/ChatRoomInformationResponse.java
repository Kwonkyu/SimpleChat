package com.example.simplechat.domains.room.bind;

import com.example.simplechat.common.bind.AuditedResponse;
import com.example.simplechat.domains.room.entity.ChatRoom;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChatRoomInformationResponse extends AuditedResponse {

	@JsonProperty("id")
	private final long id;

	@JsonProperty("manager")
	private final ChatUsernameResponse manager;

	@JsonProperty("title")
	private final String title;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	private ChatRoomInformationResponse(
		@JsonProperty("createdAt") LocalDateTime createdAt,
		@JsonProperty("updatedAt") LocalDateTime updatedAt,
		@JsonProperty("id") long id,
		@JsonProperty("manager") ChatUsernameResponse manager,
		@JsonProperty("title") String title
	) {
		super(createdAt, updatedAt);
		this.id = id;
		this.manager = manager;
		this.title = title;
	}

	private ChatRoomInformationResponse(ChatRoom chatRoom) {
		super(chatRoom.getCreatedAt(), chatRoom.getUpdatedAt());
		this.id = chatRoom.getId();
		this.manager = ChatUsernameResponse.of(chatRoom.getManager());
		this.title = chatRoom.getTitle();
	}

	public static ChatRoomInformationResponse of(ChatRoom chatRoom) {
		return new ChatRoomInformationResponse(chatRoom);
	}
}
