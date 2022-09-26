package com.midorimart.realworldapp.exception.custom;

import com.midorimart.realworldapp.model.user.mapper.CustomError;

public class CustomBadRequestException extends BaseCustomException{

	public CustomBadRequestException(CustomError custom) {
		super(custom);
		// TODO Auto-generated constructor stub
	}
	
}
