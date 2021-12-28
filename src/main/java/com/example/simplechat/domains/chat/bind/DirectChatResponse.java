package com.example.simplechat.domains.chat.bind;

import com.example.simplechat.domains.chat.entity.DirectChat;
import com.example.simplechat.domains.room.bind.ChatUsernameResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectChatResponse {

	@JsonProperty("id")
	private final Long id;

	@JsonProperty("sender")
	private final ChatUsernameResponse sender;

	@JsonProperty("receiver")
	private final ChatUsernameResponse receiver;

	@JsonProperty("message")
	private final String message;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("sentAt")
	private final LocalDateTime sentAt;

	public static DirectChatResponse of(DirectChat directChat) {
		return new DirectChatResponse(
			directChat.getId(),
			ChatUsernameResponse.of(directChat.getSender()),
			ChatUsernameResponse.of(directChat.getReceiver()),
			directChat.getMessage(),
			directChat.getCreatedAt()
		);
	}
}
