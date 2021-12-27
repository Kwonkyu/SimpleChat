package com.example.simplechat.domains.room.entity;

import com.example.simplechat.common.entity.AuditableEntity;
import com.example.simplechat.domains.chat.entity.Chat;
import com.example.simplechat.domains.user.entity.ChatUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

/**
 * Chatting room for messages and users.
 */
@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor
public class ChatRoom extends AuditableEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@OneToMany(mappedBy = "id.chatRoom", cascade = CascadeType.PERSIST)
	private final List<UserRoomRegistration> users = new ArrayList<>();

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.PERSIST)
	private final List<Chat> chats = new ArrayList<>();

	public void addUser(
		ChatUser chatUser
	) {
		UserRoomRegistration registration = UserRoomRegistration.of(chatUser, this);
		this.users.add(registration);
		chatUser.getRooms()
				.add(registration);
	}

	@Builder
	public ChatRoom(String title) {
		this.title = title;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		ChatRoom chatRoom = (ChatRoom) o;
		return id != null && Objects.equals(id, chatRoom.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"id = " + getId() + ", " +
			"createdAt = " + getCreatedAt() + ", " +
			"updatedAt = " + getUpdatedAt() + ", " +
			"title = " + getTitle() + ")";
	}
}
