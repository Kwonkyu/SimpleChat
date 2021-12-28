package com.example.simplechat.domains.room.bind;

import com.example.simplechat.domains.chat.entity.GroupChat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupChatResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("sender")
	private final ChatUsernameResponse sender;

	@JsonProperty("message")
	private final String message;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("sentAt")
	private final LocalDateTime sentAt;

	public static GroupChatResponse of(GroupChat groupChat) {
		return new GroupChatResponse(
			groupChat.getId(),
			ChatUsernameResponse.of(groupChat.getSender()),
			groupChat.getMessage(),
			groupChat.getCreatedAt()
		);
	}

}
