package com.example.simplechat.chat;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.util.JwtTokenUtil;
import com.example.simplechat.domains.chat.service.GroupChatService;
import com.example.simplechat.domains.room.bind.ChatRoomInformationResponse;
import com.example.simplechat.domains.room.bind.GroupChatRequest;
import com.example.simplechat.domains.room.bind.GroupChatResponse;
import com.example.simplechat.domains.room.bind.GroupChatsResponse;
import com.example.simplechat.domains.room.service.BasicChatRoomService;
import com.example.simplechat.domains.user.service.BasicChatUserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class GroupChatTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	BasicChatUserService chatUserService;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	BasicChatRoomService chatRoomService;
	@Autowired
	GroupChatService groupChatService;

	private static final String USER1USERNAME = "user1";
	private static String USER1TOKEN = "";
	private static String USER4TOKEN = "";
	private static final String USER2USERNAME = "user2";
	private static final String USER3USERNAME = "user3";
	private static final String USER4USERNAME = "user4";
	private static final String CHAT_FROM_USER1 = "Hello from user1";
	private static final String CHAT_FROM_USER2 = "Hello from user2";
	private static final String CHAT_FROM_USER3 = "Hello from user3";
	private static final String CHAT_FROM_USER3_LATEST = "Latest Hello from user3";
	private static final String CHAT_ROOM_TITLE = "Chat Room Title";
	private ChatRoomInformationResponse createdRoom;

	@BeforeEach
	void init() {
		chatUserService.createUser(USER1USERNAME, "alias1", "password1");
		chatUserService.createUser(USER2USERNAME, "alias2", "password2");
		chatUserService.createUser(USER3USERNAME, "alias3", "password3");
		chatUserService.createUser(USER4USERNAME, "alias4", "password4");
		USER1TOKEN = "Bearer " + jwtTokenUtil.generateAccessToken(USER1USERNAME);
		USER4TOKEN = "Bearer " + jwtTokenUtil.generateAccessToken(USER4USERNAME);
		createdRoom = chatRoomService.createChatRoom(
			USER1USERNAME, CHAT_ROOM_TITLE);
		groupChatService.createChat(createdRoom.getId(), USER1USERNAME, CHAT_FROM_USER1);
		groupChatService.createChat(createdRoom.getId(), USER2USERNAME, CHAT_FROM_USER2);
		groupChatService.createChat(createdRoom.getId(), USER3USERNAME, CHAT_FROM_USER3);
		groupChatService.createChat(createdRoom.getId(), USER3USERNAME, CHAT_FROM_USER3_LATEST);
	}

	@Test
	@DisplayName("Read group chat from not joined room.")
	void tryReadFromRoom() throws Exception {
		mockMvc.perform(get("/api/room/{roomId}/chats", createdRoom.getId())
							.header(HttpHeaders.AUTHORIZATION, USER4TOKEN))
			   .andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Read group chat from joined room.")
	void readFromRoom() throws Exception {
		mockMvc.perform(get("/api/room/{roomId}/chats", createdRoom.getId())
							.header(HttpHeaders.AUTHORIZATION, USER1TOKEN))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("result.chats").isArray())
			   .andExpect(jsonPath("result.chats").isNotEmpty())
			   .andDo(result -> {
				   ApiResponse<GroupChatsResponse> response = objectMapper.readValue(
					   result.getResponse()
							 .getContentAsString(),
					   new TypeReference<>() {
					   });
				   List<GroupChatResponse> chats = response.getResult()
														   .getChats();
				   assertEquals(4, chats.size());
				   assertTrue(chats
								  .stream()
								  .filter(r ->
											  r.getMessage()
											   .equals(CHAT_FROM_USER1))
								  .anyMatch(r ->
												r.getSender()
												 .getUsername()
												 .equals(USER1USERNAME)));
				   assertTrue(chats
								  .stream()
								  .filter(r ->
											  r.getMessage()
											   .equals(CHAT_FROM_USER2))
								  .anyMatch(r ->
												r.getSender()
												 .getUsername()
												 .equals(USER2USERNAME)));
				   assertTrue(chats
								  .stream()
								  .filter(r ->
											  r.getMessage()
											   .equals(CHAT_FROM_USER3))
								  .anyMatch(r ->
												r.getSender()
												 .getUsername()
												 .equals(USER3USERNAME)));
				   assertTrue(chats
								  .stream()
								  .filter(r ->
											  r.getMessage()
											   .equals(CHAT_FROM_USER3_LATEST))
								  .anyMatch(r ->
												r.getSender()
												 .getUsername()
												 .equals(USER3USERNAME)));
			   });
	}

	@Test
	@DisplayName("Try send chat to not joined room.")
	void trySendChatToRoom() throws Exception {
		String chat = "I'm trying to send chat!";
		GroupChatRequest request = new GroupChatRequest(chat);
		mockMvc.perform(post("/api/room/{roomId}/chats", createdRoom.getId())
							.header(HttpHeaders.AUTHORIZATION, USER4TOKEN)
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(objectMapper.writeValueAsString(request)))
			   .andExpect(status().isForbidden());
	}

	@Test
	@DisplayName("Send chat to joined room.")
	void sendChatToRoom() throws Exception {
		String chat = "I'm trying to send chat!";
		GroupChatRequest request = new GroupChatRequest(chat);
		mockMvc.perform(post("/api/room/{roomId}/chats", createdRoom.getId())
							.header(HttpHeaders.AUTHORIZATION, USER1TOKEN)
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(objectMapper.writeValueAsString(request)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("result.sender.username").value(USER1USERNAME))
			   .andExpect(jsonPath("result.message").value(chat))
			   .andExpect(jsonPath("result.id").isNotEmpty())
			   .andExpect(jsonPath("result.sentAt").isNotEmpty());
	}

}
