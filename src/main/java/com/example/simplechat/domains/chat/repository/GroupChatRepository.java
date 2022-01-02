package com.example.simplechat.domains.chat.repository;

import com.example.simplechat.domains.chat.entity.GroupChat;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GroupChatRepository extends PagingAndSortingRepository<GroupChat, Long> {

}
