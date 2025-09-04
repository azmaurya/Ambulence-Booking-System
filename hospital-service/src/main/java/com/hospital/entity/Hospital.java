package com.hospital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="hospital_service")
public class Hospital 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String location;
	private String availablebeds;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAvailablebeds() {
		return availablebeds;
	}
	public void setAvailablebeds(String availablebeds) {
		this.availablebeds = availablebeds;
	}
	public Hospital(Long id, String name, String location, String availablebeds, String username, String password) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.availablebeds = availablebeds;
	}
	@Override
	public String toString() {
		return "Hospital [id=" + id + ", name=" + name + ", location=" + location + ", availablebeds=" + availablebeds
				+  "]";
	}
	public Hospital() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
