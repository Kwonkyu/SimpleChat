package com.example.simplechat.domains.room.bind;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInformationRequest {

	@NotBlank
	private String title;

}
