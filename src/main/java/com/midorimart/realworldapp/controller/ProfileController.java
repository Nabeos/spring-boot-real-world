package com.midorimart.realworldapp.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midorimart.realworldapp.exception.custom.CustomNotFoundException;
import com.midorimart.realworldapp.model.profile.dto.ProfileDTOResponse;
import com.midorimart.realworldapp.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles/{username}")
public class ProfileController {
	private final UserService userService;
	@GetMapping("")
	public Map<String, ProfileDTOResponse> getProfile(@PathVariable String username) throws CustomNotFoundException{
		return userService.getProfile(username);
	}
	@PostMapping("/follow")
	public Map<String, ProfileDTOResponse> getFollower(@PathVariable String username) throws CustomNotFoundException{
		return userService.followUser(username);
	}
	@DeleteMapping("/follow")
	public Map<String, ProfileDTOResponse> unfollow(@PathVariable String username) throws CustomNotFoundException{
		return userService.unfollow(username);
	}
}
