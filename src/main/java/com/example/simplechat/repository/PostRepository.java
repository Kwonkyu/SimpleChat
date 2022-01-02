package com.example.simplechat.repository;

import com.example.simplechat.data.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
