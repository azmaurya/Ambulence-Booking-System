package com.user_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user_service.entities.User;
import com.user_service.payload.ApiResponse;
import com.user_service.userservice.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService; 
	}

	@PostMapping("/register")
	// @PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse> register(@RequestBody User user) {
		return userService.register(user);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody User user) {

		return userService.login(user.getUsername(), user.getPassword());
	}

	@PutMapping("/changepassword")
	public ResponseEntity<ApiResponse> changepassword(@RequestBody User user) {

		return userService.changepassword(user);
	}

	@GetMapping("/{username}")
	public ResponseEntity<Map<String, Object>> getUserName(@PathVariable String username) {
		User user = userService.getUserByUsername(username);
		userService.getUserByUsername(username);

		return ResponseEntity
				.ok(Map.of("username", user.getUsername(), "location", user.getLocation(), "role", user.getRole()));
	}

}
