package com.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String username;
	private String ambulenceDriver;
	private String hospitalName;
	private String pickupLocation;
	private String dropLocation;
	private String bookingtime;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAmbulenceDriver() {
		return ambulenceDriver;
	}

	public void setAmbulenceDriver(String ambulenceDriver) {
		this.ambulenceDriver = ambulenceDriver;
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
		return "Booking [id=" + id + ", username=" + username + ", ambulenceDriver=" + ambulenceDriver
				+ ", hospitalName=" + hospitalName + ", pickupLocation=" + pickupLocation + ", dropLocation="
				+ dropLocation + ", bookingtime=" + bookingtime + "]";
	}

	public Booking(long id, String username, String ambulenceDriver, String hospitalName, String pickupLocation,
			String dropLocation, String bookingtime) {
		super();
		this.id = id;
		this.username = username;
		this.ambulenceDriver = ambulenceDriver;
		this.hospitalName = hospitalName;
		this.pickupLocation = pickupLocation;
		this.dropLocation = dropLocation;
		this.bookingtime = bookingtime;
	}

	public String getBookingtime() {
		return bookingtime;
	}

	public void setBookingtime(String bookingtime) {
		this.bookingtime = bookingtime;
	}

	public Booking() {
		super();
		// TODO Auto-generated constructor stub
	}

}
