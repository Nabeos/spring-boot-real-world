package com.midorimart.realworldapp.model.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTOResponse {
	
	private String email;
	private String token;
	private String username;
	private String bio;
	private String image;
	
}
