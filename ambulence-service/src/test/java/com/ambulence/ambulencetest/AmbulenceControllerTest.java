package com.ambulence.ambulencetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ambulence.controller.AmbulenceController;
import com.ambulence.entity.Ambulence;
import com.ambulence.payload.ApiResponse;
import com.ambulence.service.AmbulenceService;

@ExtendWith(MockitoExtension.class)
class AmbulenceControllerTest {

	@InjectMocks
	private AmbulenceController ambulenceController;

	@Mock
	private AmbulenceService ambulenceService;

	private Ambulence sampleAmbulence;

	@BeforeEach
	void setUp() {
		sampleAmbulence = new Ambulence();
		sampleAmbulence.setId(1L);
		sampleAmbulence.setUsername("driver1");
		sampleAmbulence.setPassword("pass123");
		sampleAmbulence.setLocation("Mumbai");
		sampleAmbulence.setStatus("Available");
	}

	@Test
	void testRegisterDriver() {
		ApiResponse response = ApiResponse.builder().message("Driver registered successfully").success(true)
				.status(HttpStatus.OK).data(null).build();

		when(ambulenceService.registerDriver(sampleAmbulence)).thenReturn(ResponseEntity.ok(response));

		ResponseEntity<ApiResponse> result = ambulenceController.registerDriver(sampleAmbulence);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Driver registered successfully", result.getBody().getMessage());
		assertTrue(result.getBody().isSuccess());
	}

	@Test
	void testLogin() {
		String token = "mocked-jwt-token";

		when(ambulenceService.login("driver1", "pass123")).thenReturn(token);

		ResponseEntity<ApiResponse> result = ambulenceController.login(sampleAmbulence);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Login Successful", result.getBody().getMessage());
		assertEquals(token, result.getBody().getData());
		assertTrue(result.getBody().isSuccess());
	}

	@Test
	void testChangePassword() {
		when(ambulenceService.changepassword(sampleAmbulence)).thenReturn(sampleAmbulence);

		ResponseEntity<String> result = ambulenceController.changepassword(sampleAmbulence);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Password change successfully done", result.getBody());
	}

	@Test
	void testGetNearestAmbulence() {
		List<Ambulence> ambulences = List.of(sampleAmbulence);

		when(ambulenceService.getAmbulence("Mumbai")).thenReturn(ambulences);

		List<Ambulence> result = ambulenceController.getNearestAmbulence("Mumbai");

		assertEquals(1, result.size());
		assertEquals("driver1", result.get(0).getUsername());
	}

	@Test
	void testUpdateAmbulenceStatus() {
		doNothing().when(ambulenceService).updateStatus(1L, "Busy");

		ResponseEntity<Map<String, Object>> result = ambulenceController.updateAmbulenceStatus(1L, "Busy");

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals("Booking Status Updated", result.getBody().get("message"));
		assertEquals(1L, result.getBody().get("ambulenceId"));
		assertEquals("Busy", result.getBody().get("status"));
	}
}
