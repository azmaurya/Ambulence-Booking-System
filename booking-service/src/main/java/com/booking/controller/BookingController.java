package com.booking.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.dto.BookingRequest;
import com.booking.payload.ApiResponse;
import com.booking.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	private final BookingService bookingService;
	
	public BookingController(BookingService bookingService)
	{
		this.bookingService=bookingService;
	
	}
	
	

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> createBooking(@RequestBody BookingRequest request)
	{
		return  bookingService.register(request);
	}


	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAll()
	{
		return bookingService.getAllBooking();
	}

	@GetMapping("/users/{username}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse> getByUser(@PathVariable String username)
	{
		return bookingService.getBookingByUser(username);
	}

}
