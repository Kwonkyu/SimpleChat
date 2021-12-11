package com.example.simplechat.service;

import com.example.simplechat.controller.bind.PostCreateRequest;
import com.example.simplechat.data.Post;
import com.example.simplechat.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

	private final PostRepository postRepository;

	public Post createPost(String username,
						   PostCreateRequest request) {
		return postRepository.save(Post.builder()
						.title(request.getTitle())
						.content(request.getContent())
						.writer(username)
						.build());
	}

	public Post readPost(long postId) {
		return postRepository.findById(postId)
							 .orElseThrow(IllegalArgumentException::new);
	}
}
