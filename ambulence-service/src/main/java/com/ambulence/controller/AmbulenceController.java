package com.ambulence.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ambulence.entity.Ambulence;
import com.ambulence.payload.ApiResponse;
import com.ambulence.service.AmbulenceService;

@RestController
@RequestMapping("/driver")
public class AmbulenceController {
	@Autowired
	private AmbulenceService ambulenceService;


	@PostMapping("/register")
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<ApiResponse> registerDriver(@RequestBody Ambulence details) {
		return ambulenceService.registerDriver(details);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody Ambulence ambulence) {

		String token = ambulenceService.login(ambulence.getUsername(), ambulence.getPassword());
		ApiResponse response = ApiResponse.builder().success(true).message("Login Successful").data(token).build();
		return ResponseEntity.ok(response);
	}

	@PostMapping("/changepassword")
	public ResponseEntity<String> changepassword(@RequestBody Ambulence details) {
		ambulenceService.changepassword(details);
		return ResponseEntity.ok("Password change successfully done");
	}

	@GetMapping("/available")
	public List<Ambulence> getNearestAmbulence(@RequestParam String location) {
		return ambulenceService.getAmbulence(location);
	}

	@PutMapping("{id}/status")
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<Map<String, Object>> updateAmbulenceStatus(@PathVariable Long id,
			@RequestParam String status) 
	{
		ambulenceService.updateStatus(id, status);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Booking Status Updated");
		response.put("ambulenceId", id);
		response.put("status", status);

		return ResponseEntity.ok(response);
	}

}
