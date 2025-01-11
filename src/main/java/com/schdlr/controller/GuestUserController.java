package com.schdlr.controller;

import com.schdlr.service.GuestUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/guest")
@CrossOrigin(origins = "http://localhost:3000")
public class GuestUserController {

	private GuestUserService guestUserService;

	public GuestUserController(GuestUserService guestUserService){
		this.guestUserService = guestUserService;
	}

	@PostMapping("/signin")
	public ResponseEntity<String> signIn(HttpServletResponse response){
		try {
			return guestUserService.signIn(response);
		}catch (Exception e){
			log.error("Error during guest sign-in", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred");
		}
	}
}
