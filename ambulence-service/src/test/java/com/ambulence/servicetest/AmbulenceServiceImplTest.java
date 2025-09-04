package com.ambulence.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ambulence.entity.Ambulence;
import com.ambulence.payload.ApiResponse;
import com.ambulence.repository.AmbulenceRepository;
import com.ambulence.security.JWTHelper;
import com.ambulence.serviceimpl.AmbulenceServiceImpl;

@ExtendWith(MockitoExtension.class)
class AmbulenceServiceImplTest {

	@InjectMocks
	private AmbulenceServiceImpl ambulenceService;

	@Mock
	private AmbulenceRepository ambulenceRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JWTHelper jwtHelper;

	@Mock
	private AuthenticationManager authenticationManager;

	private Ambulence sampleAmbulence;

	@BeforeEach
	void setUp() {
		sampleAmbulence = new Ambulence();
		sampleAmbulence.setId(1L);
		sampleAmbulence.setUsername("driver1");
		sampleAmbulence.setDriverName("driver1");
		sampleAmbulence.setPassword("pass123");
		sampleAmbulence.setLocation("Mumbai");
		sampleAmbulence.setStatus("available");
	}

	@Test
	void testRegisterDriver_NewUser() {
		when(ambulenceRepository.findByUsername("driver1")).thenReturn(Optional.empty());
		when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");
		when(ambulenceRepository.save(any(Ambulence.class))).thenReturn(sampleAmbulence);

		ResponseEntity<ApiResponse> response = ambulenceService.registerDriver(sampleAmbulence);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertTrue(response.getBody().isSuccess());
		assertEquals("User registered successfully!", response.getBody().getMessage());
	}

	@Test
	void testRegisterDriver_AlreadyExists() {
		when(ambulenceRepository.findByUsername("driver1")).thenReturn(Optional.of(sampleAmbulence));

		ResponseEntity<ApiResponse> response = ambulenceService.registerDriver(sampleAmbulence);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertFalse(response.getBody().isSuccess());
		assertEquals("User already registered!", response.getBody().getMessage());
	}

	@Test
	void testLogin_Success() {
		Authentication auth = mock(Authentication.class);
		when(auth.isAuthenticated()).thenReturn(true);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

		try (MockedStatic<JWTHelper> mockedStatic = mockStatic(JWTHelper.class)) {
			mockedStatic.when(() -> JWTHelper.generateToken("driver1")).thenReturn("mocked-token");

			String token = ambulenceService.login("driver1", "pass123");

			assertEquals("mocked-token", token);
		}
	}

	@Test
	void testChangePassword_Success() {
		Ambulence existing = new Ambulence();
		existing.setUsername("driver1");
		existing.setPassword("oldPass");

		when(ambulenceRepository.findByUsername("driver1")).thenReturn(Optional.of(existing));
		when(passwordEncoder.encode("pass123")).thenReturn("newEncodedPass");
		when(ambulenceRepository.save(any(Ambulence.class))).thenReturn(existing);

		Ambulence updated = ambulenceService.changepassword(sampleAmbulence);

		assertEquals("newEncodedPass", updated.getPassword());
	}

	@Test
	void testChangePassword_SamePassword_ThrowsException() {
		Ambulence existing = new Ambulence();
		existing.setUsername("driver1");
		existing.setPassword("pass123");

		when(ambulenceRepository.findByUsername("driver1")).thenReturn(Optional.of(existing));

		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			ambulenceService.changepassword(sampleAmbulence);
		});

		assertEquals(
				"Your new password cannot be the same as your current password. Please choose a different password.",
				ex.getMessage());
	}

	@Test
	void testGetAmbulence() {
		List<Ambulence> ambulences = List.of(sampleAmbulence);
		when(ambulenceRepository.findByLocationAndStatus("Mumbai", "available")).thenReturn(ambulences);

		List<Ambulence> result = ambulenceService.getAmbulence("Mumbai");

		assertEquals(1, result.size());
		assertEquals("driver1", result.get(0).getUsername());
	}

	@Test
	void testUpdateStatus() {
		when(ambulenceRepository.findById(1L)).thenReturn(Optional.of(sampleAmbulence));
		when(ambulenceRepository.save(any(Ambulence.class))).thenReturn(sampleAmbulence);

		ambulenceService.updateStatus(1L, "Busy");

		verify(ambulenceRepository).save(sampleAmbulence);
		assertEquals("Busy", sampleAmbulence.getStatus());
	}
}
