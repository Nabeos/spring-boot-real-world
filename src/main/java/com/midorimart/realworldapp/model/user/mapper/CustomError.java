package com.midorimart.realworldapp.model.user.mapper;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomError {
	private String code;
	private String message;
	private String traces;
}
