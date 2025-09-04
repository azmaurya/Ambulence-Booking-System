package com.booking.feignclient;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.config.Myconfig;

@FeignClient(name = "ambulence-service",contextId = "AmbulenceFetchClient",configuration = Myconfig.class)
public interface FeignClientForAmbulenceService {

	@GetMapping("/driver/available")
	List<Map<String, Object>> getAmbulence(@RequestParam String location);

}
