package com.example.simplechat.registration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.simplechat.common.util.JwtTokenUtil;
import com.example.simplechat.domains.room.repository.ChatRoomRepository;
import com.example.simplechat.domains.room.service.BasicChatRoomService;
import com.example.simplechat.domains.user.bind.ChatUserResponse;
import com.example.simplechat.domains.user.repository.ChatUserRepository;
import com.example.simplechat.domains.user.service.BasicChatUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserRegistrationTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	ChatRoomRepository roomRepository;
	@Autowired
	ChatUserRepository userRepository;
	@Autowired
	BasicChatUserService chatUserService;
	@Autowired
	BasicChatRoomService chatRoomService;

	long room1Id;
	String user1Id;
	String user1Alias;
	String user1Token;

	@BeforeEach
	void init() {
		room1Id = chatRoomService.createChatRoom("ROOM_1")
								 .getId();
		ChatUserResponse user = chatUserService.createUser("USER_1", "USER1", "PASSWORD");
		user1Id = user.getUsername();
		user1Alias = user.getAlias();
		chatRoomService.joinUser(room1Id, user1Id);
		user1Token = jwtTokenUtil.generateJwt(user1Id);
	}

	@Test
	void joinUser() throws Exception {
		// given
		String user2Id = chatUserService.createUser("USER_2", "USER2", "PASSWORD")
										.getUsername();

		// when
		mockMvc
			.perform(
				put("/api/room/{roomId}/users/{userId}", room1Id, user2Id)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + user1Token))
			.andExpect(status().isOk());

		// then
		assertTrue(
			chatRoomService.getJoinedUsers(room1Id)
						   .getJoinedUsers()
						   .stream()
						   .anyMatch(user -> user.getUsername()
												 .equals(user2Id)));
	}

	@Test
	void listJoinedUsers() throws Exception {
		mockMvc
			.perform(get("/api/room/{roomId}/users", room1Id)
						 .header(HttpHeaders.AUTHORIZATION, "Bearer " + user1Token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.joinedUsers").isArray())
			.andExpect(jsonPath("$.joinedUsers.length()").value(1))
			.andExpect(jsonPath("$.joinedUsers[0].username").value(user1Id))
			.andExpect(jsonPath("$.joinedUsers[0].alias").value(user1Alias));
//			.andExpect(jsonPath("$.joinedUsers[0].joinedAt").isNotEmpty());
	}

	@Test
	void listJoinedTeams() throws Exception {
		mockMvc
			.perform(get("/api/user")
						 .header(HttpHeaders.AUTHORIZATION, "Bearer " + user1Token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value(user1Id))
			.andExpect(jsonPath("$.alias").value(user1Alias))
			.andExpect(jsonPath("$.joinedRooms").isArray())
			.andExpect(jsonPath("$.joinedRooms[0].id").value(room1Id));
	}

}
