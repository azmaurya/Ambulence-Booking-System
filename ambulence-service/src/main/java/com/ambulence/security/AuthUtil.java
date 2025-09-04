package com.ambulence.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final AuthenticationManager authenticationManager;

    public AuthUtil(AuthenticationManager authenticationManager) 
    {
        this.authenticationManager = authenticationManager;
    }

    public void authenticate(String username, String password)
    {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        if (!authentication.isAuthenticated()) 
        {
            throw new RuntimeException("Authentication failed");
        }
    }
}
