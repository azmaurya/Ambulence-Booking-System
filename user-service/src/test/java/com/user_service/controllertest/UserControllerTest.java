package com.user_service.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.user_service.controller.UserController;
import com.user_service.entities.User;
import com.user_service.payload.ApiResponse;
import com.user_service.userservice.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	private User sampleUser;

	@BeforeEach
	void setUp() {
		sampleUser = new User();
		sampleUser.setUsername("azad");
		sampleUser.setPassword("password123");
		sampleUser.setLocation("Mumbai");
		sampleUser.setRole("USER");
	}

	@Test
	void testRegisterUser() {
		ApiResponse response = ApiResponse.builder().message("User registered successfully").success(true)
				.status(HttpStatus.OK).data(null).build();

		when(userService.register(sampleUser)).thenReturn(ResponseEntity.ok(response));

		ResponseEntity<ApiResponse> result = userController.register(sampleUser);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("User registered successfully", result.getBody().getMessage());
		assertTrue(result.getBody().isSuccess());
	}

	@Test
	void testLoginUser() {
		ApiResponse response = ApiResponse.builder().message("Login successful").success(true).status(HttpStatus.OK)
				.data(null).build();

		when(userService.login("azad", "password123")).thenReturn(ResponseEntity.ok(response));

		ResponseEntity<ApiResponse> result = userController.login(sampleUser);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Login successful", result.getBody().getMessage());
		assertTrue(result.getBody().isSuccess());
	}

	@Test
	void testChangePassword() {
		ApiResponse response = ApiResponse.builder().message("Password changed successfully").success(true)
				.status(HttpStatus.OK).data(null).build();

		when(userService.changepassword(sampleUser)).thenReturn(ResponseEntity.ok(response));

		ResponseEntity<ApiResponse> result = userController.changepassword(sampleUser);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Password changed successfully", result.getBody().getMessage());
		assertTrue(result.getBody().isSuccess());
	}

	@Test
	void testGetUserName() {
		when(userService.getUserByUsername("azad")).thenReturn(sampleUser);

		ResponseEntity<Map<String, Object>> result = userController.getUserName("azad");

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("azad", result.getBody().get("username"));
		assertEquals("Mumbai", result.getBody().get("location"));
		assertEquals("USER", result.getBody().get("role"));
	}

}
