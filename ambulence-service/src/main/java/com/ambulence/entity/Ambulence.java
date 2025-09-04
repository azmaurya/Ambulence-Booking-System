package com.ambulence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ambulence_driver_details")
public class Ambulence {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String driverName;
	private String location;
	private String status;
	public Ambulence() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Ambulence [id=" + id + ", username=" + username + ", password=" + password + ", driverName="
				+ driverName + ", location=" + location + ", status=" + status + ", ambulenceNo=" + ambulenceNo + "]";
	}
	public Ambulence(Long id, String username, String password, String driverName, String location, String status,
			String ambulenceNo) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.driverName = driverName;
		this.location = location;
		this.status = status;
		this.ambulenceNo = ambulenceNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAmbulenceNo() {
		return ambulenceNo;
	}
	public void setAmbulenceNo(String ambulenceNo) {
		this.ambulenceNo = ambulenceNo;
	}
	private String ambulenceNo;

	
	
}
