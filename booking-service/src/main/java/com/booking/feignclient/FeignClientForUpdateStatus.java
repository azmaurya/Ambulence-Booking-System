package com.booking.feignclient;



import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.booking.config.Myconfig;


@FeignClient(name = "ambulence-service",contextId = "AmbulenceStatusUpdateClient" ,configuration = Myconfig.class)
public interface FeignClientForUpdateStatus 
{
	@PutMapping("/driver/{ambulenceId}/status")
	Map<String, Object> updateAmbulenceStatus(@PathVariable("ambulenceId") Integer driverId, @RequestParam String status);

}
