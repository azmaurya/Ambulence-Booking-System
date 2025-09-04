package com.user_service.userservicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.user_service.entities.User;
import com.user_service.exception.ResourceNotFoundException;
import com.user_service.exception.UserRegistrationException;
import com.user_service.payload.ApiResponse;
import com.user_service.repository.UserRepository;
import com.user_service.security.JWTHelper;
import com.user_service.userserviceimpl.UserServiceImpl;

//Test class for UserServiceImpl
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JWTHelper jwtHelper;

	@InjectMocks
	private UserServiceImpl userService;

	private User testUser;
	private User existingUser;

	@BeforeEach
	void setUp() {
		testUser = new User();
		testUser.setUsername("testuser");
		testUser.setPassword("password123");
		testUser.setRole("ROLE_USER");

		existingUser = new User();
		existingUser.setUsername("testuser");
		existingUser.setPassword("oldPassword");
		existingUser.setRole("ROLE_USER");
	}

	void testRegister_NewUser_Success() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

		ResponseEntity<ApiResponse> response = userService.register(testUser);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertTrue(response.getBody().isSuccess());
		assertEquals("User registered successfully!", response.getBody().getMessage());

		verify(userRepository).save(any(User.class));
	}

	@Test
	void testRegister_ExistingUser_Conflict() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

		ResponseEntity<ApiResponse> response = userService.register(testUser);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertFalse(response.getBody().isSuccess());
		assertEquals("User already registered!", response.getBody().getMessage());

		verify(userRepository, never()).save(any(User.class));
	}

	// failure during user save operation and expect UserRegistrationException.
	@Test
	void testRegister_SaveThrowsException() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
		doThrow(new RuntimeException("DB error")).when(userRepository).save(any(User.class));

		assertThrows(UserRegistrationException.class, () -> userService.register(testUser));
	}

	// Successful login should return a valid JWT token.
	@Test
	void testLogin_Success() {
		Authentication auth = mock(Authentication.class);
		when(auth.isAuthenticated()).thenReturn(true);
		when(authenticationManager.authenticate(any())).thenReturn(auth);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

		ResponseEntity<ApiResponse> token = userService.login("testuser", "password123");

		assertNotNull(token);
	}

	// Login with invalid credentials should throw BadCredentialsException.
	@Test
	void testLogin_InvalidCredentials() {
		when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

		assertThrows(BadCredentialsException.class, () -> {
			userService.login("testuser", "wrongpassword");
		});
	}

	// Simulate server error during login and expect RuntimeException.
	void testLogin_ServerError() {
		when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Unexpected error"));

		assertThrows(RuntimeException.class, () -> {
			userService.login("testuser", "password123");
		});
	}

	// Successfully change password when new password is different from old one.

	@Test
	void testChangePassword_Success() {
		User newUser = new User();
		newUser.setUsername("testuser");
		newUser.setPassword("newPassword");

		existingUser.setPassword("oldPassword");

		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));
		when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
		when(userRepository.save(existingUser)).thenReturn(existingUser);

		ResponseEntity<ApiResponse> result = userService.changepassword(newUser);

		ApiResponse responseBody = result.getBody();
		User updatedUser = (User) responseBody.getData();

		assertEquals("encodedNewPassword", updatedUser.getPassword());
		verify(userRepository).save(existingUser);
	}

	// Attempt to change password to the same value should throw
	// ResourceNotFoundException.
	@Test
	void testChangePassword_SamePassword_ThrowsException() {
		User newUser = new User();
		newUser.setUsername("testuser");
		newUser.setPassword("oldPassword");

		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

		assertThrows(ResourceNotFoundException.class, () -> userService.changepassword(newUser));
	}

	// Attempt to change password for non-existent user should throw
	// ResourceNotFoundException.
	@Test
	void testChangePassword_UserNotFound_ThrowsException() {
		User newUser = new User();
		newUser.setUsername("unknownuser");
		newUser.setPassword("newPassword");

		when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> userService.changepassword(newUser));
	}

	// Fetch user by username successfully when user exists.
	@Test
	void testGetUserByUsername_Success() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

		User result = userService.getUserByUsername("testuser");

		assertEquals("testuser", result.getUsername());
	}

	// Fetch user by username when user does not exist should throw
	// ResourceNotFoundException.
	@Test
	void testGetUserByUsername_UserNotFound_ThrowsException() {
		when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			userService.getUserByUsername("unknownuser");
		});

		assertEquals("User Not Found", exception.getMessage());
	}

}
