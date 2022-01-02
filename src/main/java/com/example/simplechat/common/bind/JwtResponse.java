package com.example.simplechat.common.bind;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtResponse {

	@JsonProperty("accessToken")
	private final String accessToken;

	@JsonProperty("refreshToken")
	private final String refreshToken;

}
