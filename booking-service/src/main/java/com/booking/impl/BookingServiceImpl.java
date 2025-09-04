package com.booking.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.booking.dto.BookingRequest;
import com.booking.entity.Booking;
import com.booking.exception.DatabaseException;
import com.booking.exception.ExternalServiceException;
import com.booking.exception.ResourceNotFoundException;
import com.booking.feignclient.FeignClientForAmbulenceService;
import com.booking.feignclient.FeignClientForUpdateStatus;
import com.booking.feignclient.FeignClientForUserService;
import com.booking.payload.ApiResponse;
import com.booking.repository.BookingRepository;
import com.booking.service.BookingService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private FeignClientForUserService feignClientForBookingService;

	@Autowired
	private FeignClientForAmbulenceService feignClientForAmbulenceService;

	@Autowired
	private FeignClientForUpdateStatus feignClientForUpdateStatus;

	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

	private static final String USER_SERVICE_CB = "userServiceCB";
	private static final String AMBULANCE_SERVICE_CB = "ambulanceServiceCB";

	@CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "userServiceFallback")
	public Map<String, Object> getUserDetails(String username) {
		return feignClientForBookingService.getUsername(username);
	}

	@CircuitBreaker(name = AMBULANCE_SERVICE_CB, fallbackMethod = "ambulanceServiceFallback")
	public List<Map<String, Object>> getAmbulanceDetails(String location) {
		return feignClientForAmbulenceService.getAmbulence(location);
	}

	@Override
	public ResponseEntity<ApiResponse> register(BookingRequest request) {
		Booking booking = new Booking();
		String userLocation;

		try {
			Map<String, Object> userMap = getUserDetails(request.getUsername());
			userLocation = (String) userMap.get("location");
			booking.setUsername(request.getUsername());
		} catch (Exception e) {
			throw new ExternalServiceException("User Service is currently unavailable. Please try again later.");
		}

		List<Map<String, Object>> ambulanceList;
		try {
			ambulanceList = getAmbulanceDetails(userLocation);
		} catch (Exception e) {
			throw new ExternalServiceException("Ambulance Service is currently unavailable. Please try again later.");
		}

		if (ambulanceList == null || ambulanceList.isEmpty()) {
			throw new ResourceNotFoundException("No ambulance available in your area.");
		}

		Map<String, Object> ambulance = ambulanceList.get(0);
		String driverName = (String) ambulance.get("driverName");
		Integer driverId = (Integer) ambulance.get("id");

		BeanUtils.copyProperties(request, booking);
		booking.setAmbulenceDriver(driverName);
		booking.setBookingtime(LocalDateTime.now().toString());

		try {
			bookingRepository.save(booking);
			feignClientForUpdateStatus.updateAmbulenceStatus(driverId, "busy");
		} catch (Exception e) {
			throw new DatabaseException("Error while saving booking or updating ambulance status.");
		}

		ApiResponse response = ApiResponse.builder().success(true).message("Booking created successfully").data(booking)
				.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Fallback methods
	public Map<String, Object> userServiceFallback(String username, Throwable t)

	{
		logger.error("User Service fallback triggered", t);
		throw new RuntimeException("User Service is currently unavailable. Please try again later.");
	}

	public List<Map<String, Object>> ambulanceServiceFallback(String location, Throwable t) {
		logger.error("Ambulance Service fallback triggered", t);
		throw new RuntimeException("Ambulance Service is currently unavailable. Please try again later.");
	}

	@Override
	public ResponseEntity<ApiResponse> getBookingByUser(String username) {
		try {
			List<Booking> bookings = bookingRepository.findByUsername(username);
			ApiResponse apiResponse = ApiResponse.builder().message("Bookings for user fetched successfully")
					.data(bookings).build();

			if (bookings.isEmpty()) {
				throw new ResourceNotFoundException("No bookings found for user: " + username);
			}
			return ResponseEntity.ok(apiResponse);
		} catch (Exception e) {
			throw new DatabaseException("Error while fetching bookings for user: " + username);
		}
	}

	@Override
	public ResponseEntity<ApiResponse> getAllBooking() {
		try {
			List<Booking> bookings = bookingRepository.findAll();
			if (bookings.isEmpty()) {
				throw new ResourceNotFoundException("No bookings found.");
			}

			ApiResponse apiResponse = ApiResponse.builder().message("All bookings fetched successfully").data(bookings)
					.build();

			return ResponseEntity.ok(apiResponse);
		} catch (ResourceNotFoundException e)
		{
			throw e; 
		} catch (Exception e) {
			throw new DatabaseException("Error while fetching all bookings.");
		}
	}

}
