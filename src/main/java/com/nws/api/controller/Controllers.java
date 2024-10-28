package com.nws.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class Controllers {
	
	@GetMapping("/test")
	public String getName() {
		System.out.println("Hi.. Welcome DIAM");
		return "Hi hello";
	}

}
