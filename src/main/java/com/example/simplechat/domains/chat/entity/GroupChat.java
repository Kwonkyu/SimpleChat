package com.example.simplechat.domains.chat.entity;

import com.example.simplechat.common.entity.AuditableEntity;
import com.example.simplechat.domains.room.entity.ChatRoom;
import com.example.simplechat.domains.user.entity.ChatUser;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Chat messages on room.
 */
@Entity
@Table(name = "group_chat_message")
@Getter
@NoArgsConstructor
public class GroupChat extends AuditableEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "username")
	private ChatUser sender;

	@ManyToOne
	@JoinColumn(name = "room_id", referencedColumnName = "id")
	private ChatRoom chatRoom;

	@Column(name = "content", nullable = false)
	private String message;

	@Builder
	public GroupChat(
		ChatUser sender,
		ChatRoom chatRoom,
		String message
	) {
		this.sender = sender;
		this.chatRoom = chatRoom;
		this.message = message;
	}
}
