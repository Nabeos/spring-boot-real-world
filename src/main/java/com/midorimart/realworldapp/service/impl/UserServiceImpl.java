package com.midorimart.realworldapp.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.midorimart.realworldapp.entity.User;
import com.midorimart.realworldapp.exception.custom.CustomBadRequestException;
import com.midorimart.realworldapp.exception.custom.CustomNotFoundException;
import com.midorimart.realworldapp.model.profile.dto.ProfileDTOResponse;
import com.midorimart.realworldapp.model.user.dto.UserDTOCreate;
import com.midorimart.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.midorimart.realworldapp.model.user.dto.UserDTOResponse;
import com.midorimart.realworldapp.model.user.mapper.CustomError;
import com.midorimart.realworldapp.model.user.mapper.UserMapper;
import com.midorimart.realworldapp.repository.UserRepository;
import com.midorimart.realworldapp.service.UserService;
import com.midorimart.realworldapp.utils.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository repository;
	private final JwtTokenUtil jwtTokenUtil;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap)
			throws CustomBadRequestException {
		UserDTOLoginRequest dtoLoginRequest = userLoginRequestMap.get("user");
		Optional<User> optional = repository.findByEmail(dtoLoginRequest.getEmail());
		boolean isAuthen = false;
		if (optional.isPresent()) {
			User user = optional.get();
			if (passwordEncoder.matches(dtoLoginRequest.getPassword(), user.getPassword())) {
				isAuthen = true;
			}
		}
		if (!isAuthen) {
			throw new CustomBadRequestException(
					CustomError.builder().code("400").message("Wrong username or password").build());
		}

		return buildDTOResponse(optional.get());
	}

	@Override
	public Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userCreateMap) {
		UserDTOCreate userDTOCreate = userCreateMap.get("user");
		User user = UserMapper.toUser(userDTOCreate);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = repository.save(user);
		return buildDTOResponse(user);
	}

	private Map<String, UserDTOResponse> buildDTOResponse(User user) {
		Map<String, UserDTOResponse> wrapper = new HashMap<>();
		UserDTOResponse userDTOResponse = UserMapper.toUserDTO(user);
		userDTOResponse.setToken(jwtTokenUtil.generateToken(user, 0));
		wrapper.put("user", userDTOResponse);
		return wrapper;
	}

	@Override
	public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException {
		User userLoggedIn = getUserLoggedIn();
		if (userLoggedIn != null) {
			return buildDTOResponse(userLoggedIn);
		}
		throw new CustomNotFoundException(CustomError.builder().code("404").message("not exist").build());
	}

	public User getUserLoggedIn() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String email = ((UserDetails) principal).getUsername();
			User user = repository.findByEmail(email).get();
			return user;
		}
		return null;
	}

	@Override
	public Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException {
		User userLoggedIn = getUserLoggedIn();
		Optional<User> userOptional = repository.findByUsername(username);
		if (userOptional.isEmpty()) {
			throw new CustomNotFoundException(CustomError.builder().code("404").message("user not found").build());
		}
		User user = userOptional.get();
		Set<User> followers = user.getFollowers();
		boolean isFollowing = false;
		for (User U : followers) {
			if (U.getId() == userLoggedIn.getId()) {
				isFollowing = true;
				break;
			}
		}
		return buildProfileResponse(userOptional.get(), isFollowing);
	}

	private Map<String, ProfileDTOResponse> buildProfileResponse(User user, boolean isFollowing) {
		Map<String, ProfileDTOResponse> wrapper = new HashMap<>();
		ProfileDTOResponse profileDTOResponse = ProfileDTOResponse.builder().username(user.getUsername())
				.bio(user.getBio()).image(user.getImage()).following(isFollowing).build();
		wrapper.put("profile", profileDTOResponse);
		return wrapper;
	}

	@Override
	public Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException {
		User userLoggedIn = getUserLoggedIn();
		Optional<User> optional = repository.findByUsername(username);
		if (optional.isEmpty()) {
			throw new CustomNotFoundException(CustomError.builder().code("404").message("user not exist").build());
		}
		User user = optional.get();
		Set<User> followers = user.getFollowers();
		boolean isFollowing = false;
		for (User U : followers) {
			if (U.getId() == userLoggedIn.getId()) {
				isFollowing = true;
				break;
			}
		}
		if (!isFollowing) {
			isFollowing = true;
			user.getFollowers().add(userLoggedIn);
			user = repository.save(user);
			isFollowing = true;
		}
		return buildProfileResponse(user, isFollowing);
	}

	@Override
	public Map<String, ProfileDTOResponse> unfollow(String username) throws CustomNotFoundException {
		User userLoggedIn = getUserLoggedIn();
		Optional<User> optional = repository.findByUsername(username);
		if (optional.isEmpty()) {
			throw new CustomNotFoundException(CustomError.builder().code("404").message("user not exist").build());
		}
		User user = optional.get();
		Set<User> followers = user.getFollowers();
		boolean isFollowing = false;
		for (User U : followers) {
			if (U.getId() == userLoggedIn.getId()) {
				isFollowing = true;
				break;
			}
		}
		if (!isFollowing) {
			isFollowing = true;
			user.getFollowers().remove(userLoggedIn);
			user = repository.save(user);
			isFollowing = false;
		}
		return buildProfileResponse(user, isFollowing);
	}
}
