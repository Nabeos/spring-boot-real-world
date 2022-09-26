package com.midorimart.realworldapp.model.user.mapper;

import com.midorimart.realworldapp.entity.User;
import com.midorimart.realworldapp.model.user.dto.UserDTOCreate;
import com.midorimart.realworldapp.model.user.dto.UserDTOResponse;

public class UserMapper {
	public static UserDTOResponse toUserDTO(User user) {
		return UserDTOResponse.builder().email(user.getEmail()).username(user.getUsername()).bio(user.getBio())
				.image(user.getImage()).build();
	}

	public static User toUser(UserDTOCreate userDTOCreate) {
		return User.builder().username(userDTOCreate.getUsername()).email(userDTOCreate.getEmail())
				.password(userDTOCreate.getPassword()).build();
	}
}
