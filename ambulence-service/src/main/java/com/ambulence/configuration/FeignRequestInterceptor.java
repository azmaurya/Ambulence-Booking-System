package com.ambulence.configuration;

import org.springframework.security.core.context.SecurityContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;



public class FeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template)
	{
		String token = getTokenFromSecurityContext();

		if (token != null) {

			template.header("Authorization", "Bearer " + token);

		}

	}

	private String getTokenFromSecurityContext()
	{
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentication != null) {
			Object credentials = authentication.getCredentials();
			if (credentials instanceof String) {
				return credentials.toString(); 
			}
		}

		return null;
	}

}
