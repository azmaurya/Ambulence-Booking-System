/**
 * 
 */
package com.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import feign.RequestInterceptor;

/**
 * 
 */
@Configuration
public class Myconfig {
	
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
	
	@Bean
	public RequestInterceptor requestInterceptor()
	{
		return new FeignRequestInterceptor();
	}

}
