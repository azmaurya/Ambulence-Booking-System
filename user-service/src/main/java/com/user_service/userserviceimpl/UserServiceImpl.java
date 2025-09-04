package com.user_service.userserviceimpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user_service.entities.User;
import com.user_service.exception.ResourceNotFoundException;
import com.user_service.exception.UserRegistrationException;
import com.user_service.payload.ApiResponse;
import com.user_service.repository.UserRepository;
import com.user_service.security.JWTHelper;
import com.user_service.userservice.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final JWTHelper jwtHelper;
	private final UserRepository userRepository;
	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


	public UserServiceImpl(JWTHelper jwtHelper, UserRepository userRepository,
			AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {

		this.jwtHelper = jwtHelper;
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ResponseEntity<ApiResponse> register(User user)

	{
		if (user.getRole() == null || user.getRole().isEmpty()) {
			user.setRole("ROLE_USER");
		}

		Optional<User> savedUser = userRepository.findByUsername(user.getUsername());

		if (savedUser.isPresent()) {
			int statuscode = 409;
			ApiResponse response = ApiResponse.builder().success(false).message("User already registered!")
					.status(HttpStatus.valueOf(statuscode)).build();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		try {
			userRepository.save(user);
		}

		catch (Exception e) {
			throw new UserRegistrationException("Error while saving user details.");
		}

		ApiResponse response = ApiResponse.builder().success(true).message("User registered successfully!")
				.status(HttpStatus.OK).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	public ResponseEntity<ApiResponse> login(String username, String password) {
		try {
			org.springframework.security.core.Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			if (authentication.isAuthenticated()) {
				Optional<User> user = userRepository.findByUsername(username);
				if (user.isEmpty()) {
					throw new ResourceNotFoundException("User Not Found");
				}
				String token = JWTHelper.generateToken(username, user.get().getRole());
				ApiResponse response = ApiResponse.builder().success(true).message("Login Successful").data(token)
						.status(HttpStatus.OK).build();
				return ResponseEntity.ok(response);
			} else {
				throw new BadCredentialsException("Invalid credentials");
			}
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid username or password");
		}
	}

	@Override
	public ResponseEntity<ApiResponse> changepassword(User user) {
		String username = user.getUsername();
		Optional<User> existingUser = userRepository.findByUsername(username);

		if (existingUser.isEmpty()) {
			throw new ResourceNotFoundException("User Not Found");
		}

		if (user.getPassword().equals(existingUser.get().getPassword())) {
			throw new ResourceNotFoundException(
					"Your new password cannot be the same as your current password. Please choose a different password.");
		}

		existingUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
		User save = userRepository.save(existingUser.get());
		ApiResponse response = ApiResponse.builder().success(true).message("Password change successfully done")
				.data(save).status(HttpStatus.OK).build();
		return ResponseEntity.ok(response);
	}

	@Override
	public User getUserByUsername(String username) {

		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
	}

}