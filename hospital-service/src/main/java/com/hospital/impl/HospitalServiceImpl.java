package com.hospital.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.entity.Hospital;
import com.hospital.repository.HospitalRepository;
import com.hospital.service.HospitalService;

@Service
public class HospitalServiceImpl implements HospitalService
{
	@Autowired
	private HospitalRepository hospitalRepository;

	@Override
	public Hospital addhospital(Hospital hospital)
	{
		return hospitalRepository.save(hospital);
	}

	@Override
	public List<Hospital> findByLocation(String location)
	{
		List<Hospital> byName = hospitalRepository.findByLocation(location);
		
		return byName ;
	}

	@Override
	public List<Hospital> getAllhospital() 
	{
		
		return hospitalRepository.findAll();
	}


}
