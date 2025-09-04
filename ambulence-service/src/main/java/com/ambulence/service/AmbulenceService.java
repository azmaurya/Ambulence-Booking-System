package com.ambulence.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ambulence.entity.Ambulence;
import com.ambulence.payload.ApiResponse;

public interface AmbulenceService {
	
	List<Ambulence>getAmbulence(String userLocation);
	
	ResponseEntity<ApiResponse>  registerDriver(Ambulence details);
	
	String login(String username, String password );
	
	Ambulence changepassword(Ambulence details);
	
	void updateStatus(Long driverId,String Status);

}
