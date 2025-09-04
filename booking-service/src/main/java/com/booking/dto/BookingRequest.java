package com.booking.dto;

public class BookingRequest {
	
	private String username;
	private String hospitalName;
	private String pickupLocation;
	private String dropLocation;
	private String token;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getPickupLocation() {
		return pickupLocation;
	}
	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}
	public String getDropLocation() {
		return dropLocation;
	}
	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}
	
	@Override
	public String toString() {
		return "BookingRequest [username=" + username + ", hospitalName=" + hospitalName + ", pickupLocation="
				+ pickupLocation + ", dropLocation=" + dropLocation + ", token=" + token + "]";
	}
	public BookingRequest(String username, String hospitalName, String pickupLocation, String dropLocation,
			String token) {
		super();
		this.username = username;
		this.hospitalName = hospitalName;
		this.pickupLocation = pickupLocation;
		this.dropLocation = dropLocation;
		this.token = token;
	}
	public BookingRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
