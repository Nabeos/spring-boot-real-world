package com.midorimart.realworldapp.service;

import java.util.Map;

import com.midorimart.realworldapp.exception.custom.CustomBadRequestException;
import com.midorimart.realworldapp.exception.custom.CustomNotFoundException;
import com.midorimart.realworldapp.model.profile.dto.ProfileDTOResponse;
import com.midorimart.realworldapp.model.user.dto.UserDTOCreate;
import com.midorimart.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.midorimart.realworldapp.model.user.dto.UserDTOResponse;

public interface UserService {

	Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap) throws CustomBadRequestException;

	Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userCreateMap);

	Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException;

	Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException;

	Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException;

	Map<String, ProfileDTOResponse> unfollow(String username) throws CustomNotFoundException;

}
