package com.example.simplechat.controller.bind;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {
	String title;
	String content;
	String writer;
}
