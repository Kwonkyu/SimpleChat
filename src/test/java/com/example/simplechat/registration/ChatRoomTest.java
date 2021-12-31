package com.example.simplechat.registration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.util.JwtTokenUtil;
import com.example.simplechat.domains.room.bind.ChatRoomInformationResponse;
import com.example.simplechat.domains.room.bind.JoinedUserResponse;
import com.example.simplechat.domains.room.bind.JoinedUsersResponse;
import com.example.simplechat.domains.room.bind.RoomInformationRequest;
import com.example.simplechat.domains.room.repository.ChatRoomRepository;
import com.example.simplechat.domains.room.service.BasicChatRoomService;
import com.example.simplechat.domains.user.bind.ChatUserResponse;
import com.example.simplechat.domains.user.repository.ChatUserRepository;
import com.example.simplechat.domains.user.service.BasicChatUserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChatRoomTest {

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
	@Autowired
	ChatUserRepository chatUserRepository;
	@Autowired
	ChatRoomRepository chatRoomRepository;
	@Autowired
	ObjectMapper objectMapper;

	ChatRoomInformationResponse createdChatRoom;
	ChatUserResponse user1;
	ChatUserResponse user2;
	ChatUserResponse user3;
	String user1Token;
	String user2Token;

	@BeforeEach
	void init() {
		user1 = chatUserService.createUser("USER_1", "USER1", "PASSWORD");
		createdChatRoom = chatRoomService.createChatRoom(user1.getUsername(), "ROOM_1");
		user2 = chatUserService.createUser("USER-2", "USER2", "PASSWORD");
		user1Token = "Bearer " + jwtTokenUtil.generateAccessToken(user1.getUsername());
		user2Token = "Bearer " + jwtTokenUtil.generateAccessToken(user2.getUsername());
		chatRoomService.joinUser(createdChatRoom.getId(), user2.getUsername());
		user3 = chatUserService.createUser("USER3", "USER3", "PASSWORD");
	}

	@Test
	void tryReadInformationOfNotExistingTeam() throws Exception {
		mockMvc.perform(get("/api/room/12345"))
			   .andExpect(status().isNotFound());
	}

	@Test
	void readInformationOfTeam() throws Exception {
		mockMvc.perform(get("/api/room/{roomId}", createdChatRoom.getId()))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("result.id").value(createdChatRoom.getId()))
			   .andExpect(jsonPath("result.title").value(createdChatRoom.getTitle()))
			   .andExpect(jsonPath("result.manager").exists())
			   .andExpect(jsonPath("result.manager.username").value(user1.getUsername()))
			   .andExpect(jsonPath("result.manager.alias").value(user1.getAlias()));
	}

	@Test
	void createTeam() throws Exception {
		mockMvc.perform(post("/api/room")
							.header(HttpHeaders.AUTHORIZATION, user1Token)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(
								new RoomInformationRequest("ROOM22"))))
			   .andExpect(status().isCreated())
			   .andExpect(header().exists(HttpHeaders.LOCATION))
			   .andExpect(jsonPath("result.id").exists())
			   .andExpect(jsonPath("result.manager.username").value(user1.getUsername()))
			   .andExpect(jsonPath("result.manager.alias").value(user1.getAlias()))
			   .andExpect(jsonPath("result.title").value("ROOM22"));
	}

	@Test
	void getJoinedMembers() throws Exception {
		mockMvc.perform(get("/api/room/{roomId}/users", createdChatRoom.getId())
							.header(HttpHeaders.AUTHORIZATION, user1Token))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("result.joinedUsers").isArray())
			   .andExpect(jsonPath("result.joinedUsers").isNotEmpty())
			   .andDo(result -> {
				   String content = result.getResponse()
										  .getContentAsString();
				   ApiResponse<JoinedUsersResponse> usersResponse = objectMapper.readValue(
					   content, new TypeReference<>() {
					   });
				   List<JoinedUserResponse> users = usersResponse.getResult()
																 .getJoinedUsers();
				   assertEquals(2, users.size());
				   assertEquals(1, users.stream()
										.map(JoinedUserResponse::getUsername)
										.filter(user1.getUsername()::equals)
										.count());
				   assertEquals(1, users.stream()
										.map(JoinedUserResponse::getUsername)
										.filter(user2.getUsername()::equals)
										.count());
			   });
	}

	@Test
	void inviteNotExistingMember() throws Exception {
		mockMvc.perform(put("/api/room/{roomId}/users/NOT_EXISTING_USER", createdChatRoom.getId())
							.header(HttpHeaders.AUTHORIZATION, user1Token))
			   .andExpect(status().isNotFound());
	}

	@Test
	void forbiddenRequestToInviteMember() throws Exception {
		mockMvc.perform(
			put("/api/room/{roomId}/users/{username}", createdChatRoom.getId(), user3.getUsername())
				.header(HttpHeaders.AUTHORIZATION, user2Token))
			   .andExpect(status().isForbidden());
	}

	@Test
	void duplicateMemberInvitation() throws Exception {
		mockMvc
			.perform(
				put(
					"/api/room/{roomId}/users/{username}", createdChatRoom.getId(),
					user2.getUsername()
				)
					.header(HttpHeaders.AUTHORIZATION, user1Token))
			.andExpect(status().isBadRequest());
	}

	@Test
	void inviteMemberToRoom() throws Exception {
		mockMvc
			.perform(
				put(
					"/api/room/{roomId}/users/{username}", createdChatRoom.getId(),
					user3.getUsername()
				)
					.header(HttpHeaders.AUTHORIZATION, user1Token))
			.andExpect(status().isOk())
			.andDo(result -> {
				String content = result.getResponse()
									   .getContentAsString();
				ApiResponse<JoinedUsersResponse> response = objectMapper.readValue(
					content, new TypeReference<>() {
					});
				List<JoinedUserResponse> users = response.getResult()
														 .getJoinedUsers();
				assertEquals(3, users.size());
				assertEquals(1, users.stream()
									 .map(JoinedUserResponse::getUsername)
									 .filter(user3.getUsername()::equals)
									 .count());
			});
	}

}
