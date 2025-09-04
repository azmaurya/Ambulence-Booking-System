package com.ambulence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ambulence.entity.Ambulence;


@Repository
public interface AmbulenceRepository extends JpaRepository<Ambulence, Long>
{

	List<Ambulence>findByLocationAndStatus(String userLocation, String status);
	
   Optional<Ambulence> findByUsername(String username);


	

}
