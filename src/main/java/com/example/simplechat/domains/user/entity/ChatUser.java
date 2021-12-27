package com.example.simplechat.domains.user.entity;

import com.example.simplechat.common.entity.AuditableEntity;
import com.example.simplechat.common.entity.UserRoles;
import com.example.simplechat.domains.chat.entity.Chat;
import com.example.simplechat.domains.room.entity.ChatRoom;
import com.example.simplechat.domains.room.entity.UserRoomRegistration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Chatting user. Use username for PK and identification value.
 */
@Getter
@Table(name = "chat_user")
@Entity
@NoArgsConstructor
public class ChatUser extends AuditableEntity implements UserDetails {

	@Id
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String hashedPassword;

	@Column(name = "alias", nullable = false)
	private String alias;

	@OneToMany(mappedBy = "chatUser")
	private final List<Chat> chats = new ArrayList<>();

	@OneToMany(mappedBy = "id.chatUser")
	private final List<UserRoomRegistration> rooms = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(UserRoles.ORDINARY_CHATTER.name()));
	}

	@Override
	public String getPassword() {
		return hashedPassword;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void joinRoom(
		ChatRoom chatRoom
	) {
		UserRoomRegistration registration = UserRoomRegistration.of(this, chatRoom);
		this.rooms.add(registration);
		chatRoom.getUsers()
				.add(registration);
	}

	@Builder
	public ChatUser(String username, String hashedPassword, String alias) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.alias = alias;
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
		ChatUser user = (ChatUser) o;
		return username != null && Objects.equals(username, user.username);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"username = " + getUsername() + ", " +
			"createdAt = " + getCreatedAt() + ", " +
			"updatedAt = " + getUpdatedAt() + ", " +
			"hashedPassword = " + getHashedPassword() + ", " +
			"alias = " + getAlias() + ")";
	}
}
