package com.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>
{
	List<Booking> findByUsername(String username);
}
