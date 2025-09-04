package com.ambulence.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ambulence.entity.Ambulence;
import com.ambulence.repository.AmbulenceRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private AmbulenceRepository ambulenceRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Ambulence user = ambulenceRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found!!"));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
	}

}
