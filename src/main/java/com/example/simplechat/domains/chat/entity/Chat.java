package com.example.simplechat.domains.chat.entity;

import com.example.simplechat.common.entity.AuditableEntity;
import com.example.simplechat.domains.room.entity.Room;
import com.example.simplechat.domains.user.entity.ChatUser;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Chat messages on room.
 */
@Entity
@Table(name = "chat_message")
@Getter
@NoArgsConstructor
public class Chat extends AuditableEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "username")
	private ChatUser chatUser;

	@ManyToOne
	@JoinColumn(name = "room_id", referencedColumnName = "id")
	private Room chatRoom;

	@Column(name = "content", nullable = false)
	private String message;

}
