package com.example.simplechat.domains.chat.service;

import com.example.simplechat.domains.chat.entity.GroupChat;
import com.example.simplechat.domains.room.bind.GroupChatResponse;
import com.example.simplechat.domains.room.bind.GroupChatsResponse;
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
public class GroupChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatUserRepository chatUserRepository;

	@Transactional(readOnly = true)
	public GroupChatsResponse readChats(long roomId) {
		return GroupChatsResponse.of(chatRoomRepository.findByIdOrElseThrow(roomId));
	}

	public GroupChatResponse createChat(
		long roomId,
		String senderUsername,
		String content
	) {
		ChatRoom chatRoom = chatRoomRepository.findByIdOrElseThrow(roomId);
		ChatUser user = chatUserRepository.findByUsernameOrElseThrow(senderUsername);
		GroupChat groupChat = chatRoom.addChat(user, content);
		chatRoomRepository.flush();
		return GroupChatResponse.of(groupChat);
	}

}
