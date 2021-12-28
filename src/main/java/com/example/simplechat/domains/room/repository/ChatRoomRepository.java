package com.example.simplechat.domains.room.repository;

import com.example.simplechat.domains.room.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	default ChatRoom findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Room not found."));
	}

}
