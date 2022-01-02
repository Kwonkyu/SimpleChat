package com.example.simplechat.domains.chat.entity;

import com.example.simplechat.common.entity.AuditableEntity;
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

@Entity
@Table(name = "direct_chat_message")
@Getter
@NoArgsConstructor
public class DirectChat extends AuditableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id", referencedColumnName = "username")
	private ChatUser sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", referencedColumnName = "username")
	private ChatUser receiver;

	@Column(name = "content", nullable = false)
	private String message;

	@Builder
	public DirectChat(
		ChatUser sender,
		ChatUser receiver,
		String message
	) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
	}
}
