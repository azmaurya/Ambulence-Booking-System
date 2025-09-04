package com.user_service.userservice;

import org.springframework.http.ResponseEntity;

import com.user_service.entities.User;
import com.user_service.payload.ApiResponse;

public interface UserService {

	ResponseEntity<com.user_service.payload.ApiResponse> register(User user);

	ResponseEntity<ApiResponse> login(String username, String password);

	ResponseEntity<ApiResponse> changepassword(User user);

	User getUserByUsername(String username);

}
