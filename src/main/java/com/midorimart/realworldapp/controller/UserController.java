package com.midorimart.realworldapp.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midorimart.realworldapp.exception.custom.CustomBadRequestException;
import com.midorimart.realworldapp.exception.custom.CustomNotFoundException;
import com.midorimart.realworldapp.model.user.dto.UserDTOCreate;
import com.midorimart.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.midorimart.realworldapp.model.user.dto.UserDTOResponse;
import com.midorimart.realworldapp.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService service;
	
	@PostMapping("/users/login")
	public Map<String, UserDTOResponse> login(@RequestBody Map<String, UserDTOLoginRequest> userLoginRequestMap) throws CustomBadRequestException{
		return service.authenticate(userLoginRequestMap);
	}
	@PostMapping("/users")
	public Map<String, UserDTOResponse> registerUser(@RequestBody Map<String, UserDTOCreate> userCreateMap){
		return service.registerUser(userCreateMap);
	}
	@GetMapping("/user")
	public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException{
		return service.getCurrentUser();
	}
}
