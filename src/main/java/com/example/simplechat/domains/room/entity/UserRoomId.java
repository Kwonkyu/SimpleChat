package com.example.simplechat.domains.room.entity;

import com.example.simplechat.domains.user.entity.ChatUser;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Embeddable
public class UserRoomId implements Serializable {

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "username")
	private ChatUser chatUser;

	@ManyToOne
	@JoinColumn(name = "room_id", referencedColumnName = "id")
	private Room chatRoom;

}
