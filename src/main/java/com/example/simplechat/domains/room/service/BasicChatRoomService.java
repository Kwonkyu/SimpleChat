package com.example.simplechat.domains.room.service;

import com.example.simplechat.domains.room.bind.ChatRoomInformationResponse;
import com.example.simplechat.domains.room.bind.JoinedUsersResponse;
import com.example.simplechat.domains.room.entity.ChatRoom;
import com.example.simplechat.domains.room.repository.ChatRoomRepository;
import com.example.simplechat.domains.user.entity.ChatUser;
import com.example.simplechat.domains.user.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicChatRoomService {

	private final ChatUserRepository chatUserRepository;
	private final ChatRoomRepository chatRoomRepository;

	public ChatRoomInformationResponse createChatRoom(String title) {
		return ChatRoomInformationResponse.of(
			chatRoomRepository.save(ChatRoom.builder()
											.title(title)
											.build()));
	}

	@Transactional(readOnly = true)
	public ChatRoomInformationResponse readRoom(long roomId) {
		return ChatRoomInformationResponse.of(
			chatRoomRepository
				.findById(roomId)
				.orElseThrow(
					() -> new IllegalArgumentException("Room not found.")));
	}

	@Transactional(readOnly = true)
	public JoinedUsersResponse getJoinedUsers(long roomId) {
		return JoinedUsersResponse.of(
			chatRoomRepository
				.findById(roomId)
				.orElseThrow(
					() -> new IllegalArgumentException("Room not found.")));
	}

	public JoinedUsersResponse joinUser(long roomId, String username) {
		ChatRoom room = chatRoomRepository
			.findById(roomId)
			.orElseThrow(() -> new IllegalArgumentException(
				"Team not found."));
		ChatUser user = chatUserRepository
			.findById(username)
			.orElseThrow(() -> new IllegalArgumentException(
				"User not found."));
		room.addUser(user);
		chatRoomRepository.flush();
		return JoinedUsersResponse.of(room);
	}

}
