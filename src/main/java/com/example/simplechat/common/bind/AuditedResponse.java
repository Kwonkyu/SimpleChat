package com.example.simplechat.common.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AuditedResponse {

	@JsonProperty("createdAt")
	private final LocalDateTime createdAt;

	@JsonProperty("updatedAt")
	private final LocalDateTime updatedAt;

	public AuditedResponse(LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
