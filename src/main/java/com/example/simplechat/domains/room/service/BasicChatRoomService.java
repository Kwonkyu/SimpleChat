package com.example.simplechat.domains.room.service;

import com.example.simplechat.domains.room.bind.ChatRoomInformationResponse;
import com.example.simplechat.domains.room.bind.JoinedUsersResponse;
import com.example.simplechat.domains.room.entity.ChatRoom;
import com.example.simplechat.domains.room.repository.ChatRoomRepository;
import com.example.simplechat.domains.user.entity.ChatUser;
import com.example.simplechat.domains.user.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicChatRoomService {

	private final ChatUserRepository chatUserRepository;
	private final ChatRoomRepository chatRoomRepository;

	public ChatRoomInformationResponse createChatRoom(String managerUsername, String title) {
		ChatUser manager = chatUserRepository.findByUsernameOrElseThrow(managerUsername);
		return ChatRoomInformationResponse.of(
			chatRoomRepository.save(ChatRoom.builder()
											.manager(manager)
											.title(title)
											.build()));
	}

	@Transactional(readOnly = true)
	public ChatRoomInformationResponse readRoom(long roomId) {
		return ChatRoomInformationResponse.of(chatRoomRepository.findByIdOrElseThrow(roomId));
	}

	@Transactional(readOnly = true)
	public JoinedUsersResponse getJoinedUsers(long roomId) {
		return JoinedUsersResponse.of(chatRoomRepository.findByIdOrElseThrow(roomId));
	}

	@Transactional(readOnly = true)
	public void checkManagerAuthority(String username, long roomId) {
		if(!chatRoomRepository.findByIdOrElseThrow(roomId)
								 .getManager()
								 .getUsername()
								 .equals(username)) {
			throw new AccessDeniedException(String.format("User %s is not manager of room #%d",
														  username, roomId
			));
		}
	}

	@Transactional(readOnly = true)
	public void checkJoinedAuthority(String username, long roomId) {
		ChatUser user = chatUserRepository.findByUsernameOrElseThrow(username);
		ChatRoom room = chatRoomRepository.findByIdOrElseThrow(roomId);
		if(!room.containsUser(user)) {
			throw new AccessDeniedException(String.format("User %s is not joined to room #%d",
														  username, roomId
			));
		}
	}

	public JoinedUsersResponse joinUser(long roomId, String username) {
		ChatRoom room = chatRoomRepository.findByIdOrElseThrow(roomId);
		ChatUser user = chatUserRepository.findByUsernameOrElseThrow(username);
		room.addUser(user);
		chatRoomRepository.flush();
		return JoinedUsersResponse.of(room);
	}

}
