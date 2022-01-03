package com.example.simplechat.domains.chat.broker;

import com.example.simplechat.domains.chat.service.GroupChatService;
import com.example.simplechat.domains.room.bind.GroupChatRequest;
import com.example.simplechat.domains.room.bind.GroupChatResponse;
import com.example.simplechat.domains.room.bind.JoinedUserResponse;
import com.example.simplechat.domains.room.service.BasicChatRoomService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GroupChatBroker {

	private final BasicChatRoomService chatRoomService;
	private final GroupChatService groupChatService;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chats/group/{roomId}")
	@SendToUser("/chats")
	public GroupChatResponse handleGroupChatRequest(
		Principal principal,
		@DestinationVariable("roomId") long roomId,
		@Payload GroupChatRequest request
	) {
		chatRoomService.checkJoinedAuthority(principal.getName(), roomId);
		GroupChatResponse chat = groupChatService.createChat(
			roomId, principal.getName(), request.getMessage());
		chatRoomService.getJoinedUsers(roomId)
					   .getJoinedUsers()
					   .stream()
					   .map(JoinedUserResponse::getUsername)
					   .forEach(username ->
									messagingTemplate.convertAndSendToUser(
										username, "/chats", chat));
		return chat;
	}
}
