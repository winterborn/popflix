package com.project.popflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class PopflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(PopflixApplication.class, args);
	}

	@GetMapping("/hello")
	public String homepage() {
		return "HELLO";
	}

}
