package com.example.simplechat.domains.chat.repository;

import com.example.simplechat.domains.chat.entity.DirectChat;
import com.example.simplechat.domains.chat.exception.DirectChatNotFoundException;
import com.example.simplechat.domains.user.entity.ChatUser;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DirectChatRepository extends CrudRepository<DirectChat, Long> {

	default DirectChat findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new DirectChatNotFoundException(id));
	}

	// https://stackoverflow.com/questions/38410702/group-by-sender-receiver-or-receiver-sender-in-mysql
	@Query(
		"SELECT c FROM DirectChat c WHERE c.id in ("
			+ "SELECT max(c_.id) FROM DirectChat c_ WHERE c_.sender = ?1 OR c_.receiver = ?1 "
			+ "GROUP BY "
			+ "CASE WHEN (c_.sender = ?1) THEN c_.sender ELSE c_.receiver END, "
			+ "CASE WHEN (c_.sender = ?1) THEN c_.receiver ELSE c_.sender END"
			+ ")")
	List<DirectChat> findAllByReceiverGroup(ChatUser sender);

	@Query("SELECT c FROM DirectChat c WHERE "
		+ "c.sender = ?1 AND c.receiver = ?2 OR "
		+ "c.sender = ?2 AND c.receiver = ?1 "
		+ "ORDER BY c.id DESC")
	List<DirectChat> findChatsBetween(ChatUser user1, ChatUser user2);

}
