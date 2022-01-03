package com.example.simplechat.domains.chat.service;

import com.example.simplechat.domains.chat.bind.DirectChatResponse;
import com.example.simplechat.domains.chat.bind.DirectChatsResponse;
import com.example.simplechat.domains.chat.entity.DirectChat;
import com.example.simplechat.domains.chat.repository.DirectChatRepository;
import com.example.simplechat.domains.user.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectChatService {

	private final ChatUserRepository chatUserRepository;
	private final DirectChatRepository directChatRepository;

	@Transactional(readOnly = true)
	public DirectChatsResponse readMyChats(String username) {
		return DirectChatsResponse.of(
			directChatRepository.findAllByReceiverGroup(
				chatUserRepository.findByUsernameOrElseThrow(username)));
	}

	@Transactional(readOnly = true)
	public DirectChatResponse readChat(long chatId) {
		return DirectChatResponse.of(
			directChatRepository.findByIdOrElseThrow(chatId));
	}

	public DirectChatResponse createChat(
		String senderUsername,
		String receiverUsername,
		String message
	) {
		return DirectChatResponse.of(
			directChatRepository.save(
				DirectChat.builder()
						  .sender(chatUserRepository.findByUsernameOrElseThrow(senderUsername))
						  .receiver(chatUserRepository.findByUsernameOrElseThrow(receiverUsername))
						  .message(message)
						  .build()));
	}

	@Transactional(readOnly = true)
	public DirectChatsResponse readChatsBetween(
		String user1,
		String user2
	) {
		return DirectChatsResponse.of(
			directChatRepository.findChatsBetween(
				chatUserRepository.findByUsernameOrElseThrow(user1),
				chatUserRepository.findByUsernameOrElseThrow(user2)
			));
	}

}
