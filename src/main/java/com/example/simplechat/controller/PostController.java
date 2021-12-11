package com.example.simplechat.controller;

import com.example.simplechat.controller.bind.PostCreateRequest;
import com.example.simplechat.controller.bind.PostCreationNotification;
import com.example.simplechat.data.Post;
import com.example.simplechat.security.JwtAuthentication;
import com.example.simplechat.service.PostService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

	private final PostService postService;
	private final SimpMessagingTemplate messagingTemplate;

	@PostMapping
	public ResponseEntity<Post> createPost(@AuthenticationPrincipal JwtAuthentication auth,
										   @RequestBody PostCreateRequest request) {
		Post post = postService.createPost(auth.getUsername(), request);
		messagingTemplate.convertAndSendToUser(
			"member1",
			"/posts",
			new PostCreationNotification(post.getWriter(), post.getTitle()));
		messagingTemplate.convertAndSendToUser(
			"member2",
			"/posts",
			new PostCreationNotification(post.getWriter(), post.getTitle()));
		return ResponseEntity.created(URI.create("/posts/" + post.getId()))
							 .body(post);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Post> readPost(@PathVariable("postId") long postId) {
		return ResponseEntity.ok(postService.readPost(postId));
	}

}
