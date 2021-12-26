package com.example.simplechat.domains.room.entity;

import com.example.simplechat.domains.user.entity.ChatUser;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserRoomId implements Serializable {

	@ManyToOne
	@JoinColumn(name = "joined_user_id", referencedColumnName = "username")
	private ChatUser chatUser;

	@ManyToOne
	@JoinColumn(name = "joined_room_id", referencedColumnName = "id")
	private ChatRoom chatRoom;

	public static UserRoomId of(ChatUser user, ChatRoom room) {
		return new UserRoomId(user, room);
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
		UserRoomId that = (UserRoomId) o;
		return chatUser != null && Objects.equals(chatUser, that.chatUser)
			&& chatRoom != null && Objects.equals(chatRoom, that.chatRoom);
	}

	@Override
	public int hashCode() {
		return Objects.hash(chatUser, chatRoom);
	}
}
