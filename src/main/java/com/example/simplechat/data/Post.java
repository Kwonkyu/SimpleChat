package com.example.simplechat.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String writer;
	private String title;
	private String content;

	@Builder
	public Post(String writer, String title, String content) {
		this.writer = writer;
		this.title = title;
		this.content = content;
	}
}
