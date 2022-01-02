package com.example.simplechat.domains.room.controller;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.domains.room.bind.ChatRoomInformationResponse;
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

	@PostMapping
	public ResponseEntity<ApiResponse<ChatRoomInformationResponse>> createRoom(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@Valid @RequestBody RoomInformationRequest request
	) {
		return ResponseEntity
			.created(URI.create("/location"))
			.body(ApiResponse.success(
				chatRoomService.createChatRoom(authentication.getUsername(), request.getTitle())));
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<ApiResponse<ChatRoomInformationResponse>> readRoom(
		@PathVariable("roomId") long roomId
	) {
		return ResponseEntity.ok(
			ApiResponse.success(chatRoomService.readRoom(roomId)));
	}

	@GetMapping("/{roomId}/users")
	public ResponseEntity<ApiResponse<JoinedUsersResponse>> getJoinedUsers(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("roomId") long roomId
	) {
		chatRoomService.checkJoinedAuthority(authentication.getUsername(), roomId);
		return ResponseEntity.ok(
			ApiResponse.success(chatRoomService.getJoinedUsers(roomId)));
	}

	@PutMapping("/{roomId}/users/{username}")
	public ResponseEntity<ApiResponse<JoinedUsersResponse>> addUserToRoom(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("roomId") long roomId,
		@PathVariable("username") String username
	) {
		chatRoomService.checkManagerAuthority(authentication.getUsername(), roomId);
		return ResponseEntity.ok(
			ApiResponse.success(chatRoomService.joinUser(roomId, username)));
	}

}
