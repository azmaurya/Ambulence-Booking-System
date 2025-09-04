package com.hospital.service;

import java.util.List;

import com.hospital.entity.Hospital;

public interface HospitalService {
	
	Hospital addhospital(Hospital hospital);
	
	List<Hospital> findByLocation(String location);
	
	List<Hospital> getAllhospital( );

}
