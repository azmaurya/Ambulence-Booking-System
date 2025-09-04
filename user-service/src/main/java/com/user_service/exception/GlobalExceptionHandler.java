package com.user_service.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.booking.payload.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
		ApiResponse response = ApiResponse.builder().success(false).message(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(UserRegistrationException.class)
	public ResponseEntity<ApiResponse> handleUserRegistrationException(UserRegistrationException ex) {
		ApiResponse response = ApiResponse.builder().success(false).message(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
		ApiResponse response = ApiResponse.builder().success(false).message("Something went wrong: " + ex.getMessage())
				.build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElement(NoSuchElementException ex) {
		return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
	}

	/*
	 * @ExceptionHandler(BadRequestException.class) public
	 * ResponseEntity<ApiResponse> handleBadrequest(BadRequestException e) { return
	 * new ResponseEntity<>(new ApiResponse(ex.getMessage(),false,
	 * null),HttpStatus.BAD_REQUEST); }
	 */

}
