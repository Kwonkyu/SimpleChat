package com.example.simplechat.domains.room.entity;

import com.example.simplechat.common.entity.AuditableEntity;
import com.example.simplechat.domains.user.entity.ChatUser;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Chat user and room association mapping. If user has ever entered the room,
 * this record is created. Even user withdraw from room record won't be deleted
 * and use 'joined' field to indicate whether user is still joining chat room or not.
 */
@Getter
@Table(name = "user_room_registration")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class UserRoomRegistration extends AuditableEntity {

	@EmbeddedId
	private UserRoomId id;

	@Column(name = "joined", nullable = false)
	private boolean joined;

	public static UserRoomRegistration of(ChatUser user, ChatRoom room) {
		return new UserRoomRegistration(UserRoomId.of(user, room), true);
	}

	public ChatUser getUser() {
		return id.getChatUser();
	}

	public ChatRoom getRoom() {
		return id.getChatRoom();
	}

	public boolean isJoined() {
		return joined;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"EmbeddedId = " + getId() + ", " +
			"createdAt = " + getCreatedAt() + ", " +
			"updatedAt = " + getUpdatedAt() + ", " +
			"joined = " + isJoined() + ")";
	}
}
