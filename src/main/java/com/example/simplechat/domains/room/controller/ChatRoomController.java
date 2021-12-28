package com.example.simplechat.domains.room.controller;

import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.domains.chat.service.GroupChatService;
import com.example.simplechat.domains.room.bind.GroupChatRequest;
import com.example.simplechat.domains.room.bind.ChatRoomInformationResponse;
import com.example.simplechat.domains.room.bind.GroupChatResponse;
import com.example.simplechat.domains.room.bind.GroupChatsResponse;
import com.example.simplechat.domains.room.bind.JoinedUsersResponse;
import com.example.simplechat.domains.room.bind.RoomInformationRequest;
import com.example.simplechat.domains.room.service.BasicChatRoomService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room")
public class ChatRoomController {

	private final BasicChatRoomService chatRoomService;
	private final GroupChatService groupChatService;

	@PostMapping
	public ResponseEntity<ChatRoomInformationResponse> createRoom(
		@Valid @RequestBody RoomInformationRequest request
	) {
		return ResponseEntity
			.created(URI.create("/location"))
			.body(chatRoomService.createChatRoom(request.getTitle()));
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<ChatRoomInformationResponse> readRoom(
		@PathVariable("roomId") long roomId
	) {
		return ResponseEntity.ok(chatRoomService.readRoom(roomId));
	}

	@GetMapping("/{roomId}/users")
	public ResponseEntity<JoinedUsersResponse> getJoinedUsers(
		@PathVariable("roomId") long roomId
	) {
		return ResponseEntity.ok(chatRoomService.getJoinedUsers(roomId));
	}

	@PutMapping("/{roomId}/users/{username}")
	public ResponseEntity<JoinedUsersResponse> addUserToRoom(
		@PathVariable("roomId") long roomId,
		@PathVariable("username") String username
	) {
		return ResponseEntity.ok(chatRoomService.joinUser(roomId, username));
	}

	@GetMapping("/{roomId}/chats")
	public ResponseEntity<GroupChatsResponse> getChats(
		@PathVariable("roomId") long roomId
	) {
		return ResponseEntity.ok(groupChatService.readChats(roomId));
	}

	@PostMapping("/{roomId}/chats")
	public ResponseEntity<GroupChatResponse> sendChat(
		@PathVariable("roomId") long roomId,
		@AuthenticationPrincipal JwtAuthentication authentication,
		@Valid @RequestBody GroupChatRequest groupChatRequest
	) {
		return ResponseEntity.ok(groupChatService.createChat(
			roomId,
			authentication.getUsername(),
			groupChatRequest.getMessage()
		));
	}

}
