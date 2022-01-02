package com.example.simplechat.domains.chat.controller;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.config.jwt.JwtAuthentication;
import com.example.simplechat.domains.chat.service.GroupChatService;
import com.example.simplechat.domains.room.bind.GroupChatRequest;
import com.example.simplechat.domains.room.bind.GroupChatResponse;
import com.example.simplechat.domains.room.bind.GroupChatsResponse;
import com.example.simplechat.domains.room.service.BasicChatRoomService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room/{roomId}/chats")
public class GroupChatController {

	private final BasicChatRoomService chatRoomService;
	private final GroupChatService groupChatService;

	@GetMapping
	public ResponseEntity<ApiResponse<GroupChatsResponse>> getChats(
		@AuthenticationPrincipal JwtAuthentication authentication,
		@PathVariable("roomId") long roomId
	) {
		chatRoomService.checkJoinedAuthority(authentication.getUsername(), roomId);
		return ResponseEntity.ok(
			ApiResponse.success(groupChatService.readChats(roomId)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<GroupChatResponse>> sendChat(
		@PathVariable("roomId") long roomId,
		@AuthenticationPrincipal JwtAuthentication authentication,
		@Valid @RequestBody GroupChatRequest groupChatRequest
	) {
		chatRoomService.checkJoinedAuthority(authentication.getUsername(), roomId);
		return ResponseEntity.ok(
			ApiResponse.success(
				groupChatService.createChat(
					roomId,
					authentication.getUsername(),
					groupChatRequest.getMessage()
				)));
	}
}
