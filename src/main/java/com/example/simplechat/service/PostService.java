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

	public Post createPost(PostCreateRequest request) {
		Post post = Post.builder()
						.title(request.getTitle())
						.content(request.getContent())
						.writer(request.getWriter())
						.build();
		return postRepository.save(post);
	}

	public Post readPost(long postId) {
		return postRepository.findById(postId)
							 .orElseThrow(IllegalArgumentException::new);
	}
}
