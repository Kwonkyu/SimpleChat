package com.example.simplechat.domains.room.entity;

import com.example.simplechat.common.entity.AuditableEntity;
import com.example.simplechat.domains.chat.entity.Chat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Chatting room for messages and users.
 */
@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor
public class Room extends AuditableEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@OneToMany(mappedBy = "id.chatRoom")
	private final List<UserRoomRegistration> users = new ArrayList<>();

	@OneToMany(mappedBy = "chatRoom")
	private final List<Chat> chats = new ArrayList<>();

}
