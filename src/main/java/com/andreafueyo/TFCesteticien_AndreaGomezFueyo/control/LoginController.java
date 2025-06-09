package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class LoginController {
	@GetMapping("/login")
	String login() {
		return "login";
	}
}
