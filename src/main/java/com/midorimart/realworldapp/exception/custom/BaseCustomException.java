package com.midorimart.realworldapp.exception.custom;

import java.util.HashMap;
import java.util.Map;

import com.midorimart.realworldapp.model.user.mapper.CustomError;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BaseCustomException extends Exception{
	private Map<String, CustomError> error;

	public BaseCustomException(CustomError custom) {
		this.error = new HashMap<>();
		this.error.put("error", custom);
	}
	
}
