package com.example.simplechat;

import static org.junit.jupiter.api.Assertions.*;

import com.example.simplechat.domains.room.entity.ChatRoom;
import com.example.simplechat.domains.room.entity.UserRoomRegistration;
import com.example.simplechat.domains.room.repository.ChatRoomRepository;
import com.example.simplechat.domains.user.entity.ChatUser;
import com.example.simplechat.domains.user.repository.ChatUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SimpleChatApplicationTests {

	@Autowired
	ChatUserRepository chatUserRepository;
	@Autowired
	ChatRoomRepository chatRoomRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	private long roomId;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void joinUserToRoom() {
		ChatUser chatUser = chatUserRepository.save(
			ChatUser
				.builder()
				.username("CHAT_USER1")
				.alias("CHAT_USER1_ALIAS")
				.hashedPassword(
					new BCryptPasswordEncoder(10).encode(
						"CHAT_USER1_PASSWORD"))
				.build());
		ChatRoom room = chatRoomRepository.save(
			ChatRoom.builder()
					.title("ROOM_1")
					.build());
		chatUser.joinRoom(room);
		roomId = room.getId();
	}

	@Test
	void readJoinStatusTest() {
		joinUserToRoom();
		ChatUser user = chatUserRepository.findById("CHAT_USER1")
										  .orElseThrow(IllegalArgumentException::new);
		ChatRoom room = chatRoomRepository.findById(roomId)
										  .orElseThrow(IllegalArgumentException::new);

		assertEquals(1, user.getRooms()
							.size());
		assertTrue(user.getRooms()
					   .stream()
					   .map(UserRoomRegistration::getId)
					   .anyMatch(
						   registration ->
							   registration.getChatUser()
										   .equals(user) &&
								   registration.getChatRoom()
											   .equals(room)));

		assertEquals(1, room.getUsers()
							.size());
		assertTrue(room.getUsers()
					   .stream()
					   .map(UserRoomRegistration::getId)
					   .anyMatch(
						   registration ->
							   registration.getChatUser()
										   .equals(user) &&
								   registration.getChatRoom()
											   .equals(room)));
	}

}
