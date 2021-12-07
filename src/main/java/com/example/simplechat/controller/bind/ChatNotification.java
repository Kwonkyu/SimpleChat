package com.example.simplechat.controller.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {
	private String sender;
	private String message;
}
