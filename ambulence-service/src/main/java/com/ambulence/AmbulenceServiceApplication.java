package com.ambulence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
public class AmbulenceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmbulenceServiceApplication.class, args);
	}

}
