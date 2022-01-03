package com.example.simplechat.chat;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.simplechat.common.bind.ApiResponse;
import com.example.simplechat.common.util.JwtTokenUtil;
import com.example.simplechat.domains.chat.bind.DirectChatRequest;
import com.example.simplechat.domains.chat.bind.DirectChatResponse;
import com.example.simplechat.domains.chat.bind.DirectChatsResponse;
import com.example.simplechat.domains.chat.service.DirectChatService;
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
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DirectChatTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	BasicChatUserService chatUserService;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	DirectChatService directChatService;

	private static final String USER1USERNAME = "user1";
	private static String USER1TOKEN = "";
	private static final String USER2USERNAME = "user2";
	private static final String USER3USERNAME = "user3";
	private static final String CHAT_FROM_USER1_TO_USER2 = "Hello from user1 to user2";
	private static final String CHAT_FROM_USER2_TO_USER1 = "Hello from user2 to user1";
	private static final String CHAT_FROM_USER3_TO_USER1 = "Hello from user3 to user1";
	private static final String CHAT_FROM_USER3_TO_USER1_LATEST = "Latest Hello from user3 to user1";

	@BeforeEach
	void init() {
		chatUserService.createUser(USER1USERNAME, "alias1", "password1");
		chatUserService.createUser(USER2USERNAME, "alias2", "password2");
		chatUserService.createUser(USER3USERNAME, "alias3", "password3");
		USER1TOKEN = "Bearer " + jwtTokenUtil.generateAccessToken(USER1USERNAME);
		directChatService.createChat(USER1USERNAME, USER2USERNAME, CHAT_FROM_USER1_TO_USER2);
		directChatService.createChat(USER2USERNAME, USER1USERNAME, CHAT_FROM_USER2_TO_USER1);
		directChatService.createChat(USER2USERNAME, USER1USERNAME, CHAT_FROM_USER2_TO_USER1);
		directChatService.createChat(USER1USERNAME, USER2USERNAME, CHAT_FROM_USER1_TO_USER2);
		directChatService.createChat(USER3USERNAME, USER1USERNAME, CHAT_FROM_USER3_TO_USER1);
		directChatService.createChat(USER3USERNAME, USER1USERNAME, CHAT_FROM_USER3_TO_USER1);
		directChatService.createChat(USER3USERNAME, USER1USERNAME, CHAT_FROM_USER3_TO_USER1_LATEST);
	}

	@Test
	@DisplayName("Send chat to another user.")
	void sendChat() throws Exception {
		mockMvc.perform(post("/api/chat/{receiver}", USER2USERNAME)
							.header(HttpHeaders.AUTHORIZATION, USER1TOKEN)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(
								new DirectChatRequest("Hello there!"))))
			   .andExpect(status().isCreated())
			   .andExpect(header().exists(HttpHeaders.LOCATION))
			   .andExpect(jsonPath("result.id").isNumber())
			   .andExpect(jsonPath("result.sender.username").value(USER1USERNAME))
			   .andExpect(jsonPath("result.receiver.username").value(USER2USERNAME))
			   .andExpect(jsonPath("result.message").value("Hello there!"))
			   .andExpect(jsonPath("result.sentAt").exists());
	}

	@Test
	@DisplayName("Read chats between user.")
	void readChatsBetween() throws Exception {
		mockMvc.perform(get("/api/chat/{receiver}", USER2USERNAME)
							.header(HttpHeaders.AUTHORIZATION, USER1TOKEN))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("result.chats").isArray())
			   .andExpect(jsonPath("result.chats").isNotEmpty())
			   .andDo(result -> {
				   String content = result.getResponse()
										  .getContentAsString();
				   ApiResponse<DirectChatsResponse> response = objectMapper.readValue(
					   content, new TypeReference<>() {
					   });
				   List<DirectChatResponse> chatResponses = response.getResult()
																	.getChatResponses();
				   assertEquals(4, chatResponses.size());
				   assertEquals(2L, chatResponses
					   .stream()
					   .filter(r -> r.getSender()
									 .getUsername()
									 .equals(USER1USERNAME))
					   .map(DirectChatResponse::getMessage)
					   .filter(CHAT_FROM_USER1_TO_USER2::equals)
					   .count());
				   assertEquals(2L, chatResponses
					   .stream()
					   .filter(r -> r.getSender()
									 .getUsername()
									 .equals(USER2USERNAME))
					   .map(DirectChatResponse::getMessage)
					   .filter(CHAT_FROM_USER2_TO_USER1::equals)
					   .count());
			   });
	}

	@Test
	@DisplayName("Get chats by group.")
	void getChatsByGroup() throws Exception {
		mockMvc.perform(get("/api/chat")
							.header(HttpHeaders.AUTHORIZATION, USER1TOKEN))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("result.chats").isArray())
			   .andExpect(jsonPath("result.chats").isNotEmpty())
			   .andDo(result -> {
				   String content = result.getResponse()
										  .getContentAsString();
				   ApiResponse<DirectChatsResponse> response = objectMapper.readValue(
					   content, new TypeReference<>() {
					   });
				   List<DirectChatResponse> chats = response.getResult()
															.getChatResponses();
				   assertEquals(2, chats.size());
				   assertEquals(1, chats.stream()
										.filter(chat -> chat.getSender()
															.getUsername()
															.equals(USER1USERNAME))
										.filter(chat -> chat.getReceiver()
															.getUsername()
															.equals(USER2USERNAME))
										.filter(chat -> chat.getMessage()
															.equals(CHAT_FROM_USER1_TO_USER2))
										.count());
				   assertEquals(1, chats.stream()
										.filter(chat -> chat.getSender()
															.getUsername()
															.equals(USER3USERNAME))
										.filter(chat -> chat.getReceiver()
															.getUsername()
															.equals(USER1USERNAME))
										.filter(chat -> chat.getMessage()
															.equals(
																CHAT_FROM_USER3_TO_USER1_LATEST))
										.count());
			   });
	}
}
