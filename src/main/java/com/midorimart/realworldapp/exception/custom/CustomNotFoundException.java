package com.midorimart.realworldapp.exception.custom;

import com.midorimart.realworldapp.model.user.mapper.CustomError;

public class CustomNotFoundException extends BaseCustomException{

	public CustomNotFoundException(CustomError custom) {
		super(custom);
		// TODO Auto-generated constructor stub
	}
	
}
