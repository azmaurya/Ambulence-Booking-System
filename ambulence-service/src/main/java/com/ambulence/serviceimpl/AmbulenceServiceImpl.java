package com.ambulence.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ambulence.entity.Ambulence;
import com.ambulence.exception.DriverRegistrationException;
import com.ambulence.payload.ApiResponse;
import com.ambulence.repository.AmbulenceRepository;
import com.ambulence.security.JWTHelper;
import com.ambulence.service.AmbulenceService;

@Service
public class AmbulenceServiceImpl implements AmbulenceService {
	@Autowired
	private AmbulenceRepository ambulenceRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTHelper jwtHelper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public ResponseEntity<ApiResponse> registerDriver(Ambulence ambulence) {

		Optional<Ambulence> savedUser = ambulenceRepository.findByUsername(ambulence.getUsername());

		if (savedUser.isPresent()) {
			ApiResponse response = ApiResponse.builder().success(false).message("User already registered!").build();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
		}

		ambulence.setPassword(passwordEncoder.encode(ambulence.getPassword()));
		try {
			ambulenceRepository.save(ambulence);
		}

		catch (Exception e) {
			throw new DriverRegistrationException("Error while saving Driver details.");
		}
		ApiResponse response = ApiResponse.builder().success(true).message("User registered successfully!").build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	public String login(String username, String password) {
		try {
			org.springframework.security.core.Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			if (authentication.isAuthenticated()) {
				return JWTHelper.generateToken(username);
			}

			else {
				throw new BadCredentialsException("Invalid Credential");
			}
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid username or password");
		} catch (Exception e) {
			throw new RuntimeException("Login failed due to server error");
		}

	}

	@Override
	public Ambulence changepassword(Ambulence ambulce) {
		String username = ambulce.getDriverName();
		Optional<Ambulence> existingUser = ambulenceRepository.findByUsername(username);

		if (username.equals(existingUser.get().getUsername())) {
			if (ambulce.getPassword().equals(existingUser.get().getPassword())) {
				throw new RuntimeException(
						"Your new password cannot be the same as your current password. Please choose a different password.");
			}
			existingUser.get().setPassword(passwordEncoder.encode(ambulce.getPassword()));
			return ambulenceRepository.save(existingUser.get());
		}

		throw new RuntimeException("User Not found");
	}

	@Override
	public List<Ambulence> getAmbulence(String userLocation) {

		return ambulenceRepository.findByLocationAndStatus(userLocation, "available");
	}

	@Override
	public void updateStatus(Long driverId, String status) {
		Optional<Ambulence> driver = ambulenceRepository.findById(driverId);
		driver.ifPresent(i -> {
			i.setStatus(status);

			ambulenceRepository.save(i);
		});
	}

}
