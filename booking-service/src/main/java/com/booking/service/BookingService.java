package com.booking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.booking.dto.BookingRequest;
import com.booking.entity.Booking;
import com.booking.payload.ApiResponse;

public interface BookingService {
	
	//Booking createBooking(Booking request);
	
	ResponseEntity<ApiResponse> getBookingByUser(String username);
	
	ResponseEntity<ApiResponse> getAllBooking();

	ResponseEntity<ApiResponse> register(BookingRequest request);
	
	//List<Booking>getDiverBooking(String driver);
	
	//List<Booking>getHospitalBooking(String hospital);
	
	//String updateAmbulenceStatus(long bookingid,String status);
	
	

}
