package com.example.simplechat.domains.room.repository;

import com.example.simplechat.domains.room.entity.ChatRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

}
