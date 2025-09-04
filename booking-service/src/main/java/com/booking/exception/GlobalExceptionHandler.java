package com.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.booking.payload.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse response = ApiResponse.builder()
            .success(false)
            .message(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponse> handleExternalServiceError(ExternalServiceException ex) {
        ApiResponse response = ApiResponse.builder()
            .success(false)
            .message(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse> handleDatabaseError(DatabaseException ex) {
        ApiResponse response = ApiResponse.builder()
            .success(false)
            .message(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericError(Exception ex) {
        ApiResponse response = ApiResponse.builder()
            .success(false)
            .message("Unexpected error occurred: " + ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
