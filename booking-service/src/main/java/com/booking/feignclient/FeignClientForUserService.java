package com.booking.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.booking.config.Myconfig;



@FeignClient(name = "user-service", configuration = Myconfig.class)
public interface FeignClientForUserService {

    @GetMapping("/users/{username}")
    Map<String, Object> getUsername(@PathVariable String username);
}
