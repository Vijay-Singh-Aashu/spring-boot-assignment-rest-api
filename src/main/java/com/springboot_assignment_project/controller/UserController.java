package com.springboot_assignment_project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot_assignment_project.dto.UserDto;
import com.springboot_assignment_project.model.User;
import com.springboot_assignment_project.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDto userDto) {
		Map<String, String> response = new HashMap<>();
		try {
			// Register the new user
			userService.registerNewUser(userDto);
			response.put("message", "Registration successful. Check your email for verification purpose.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "Registration failed: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/verify/{token}")
	public ResponseEntity<Map<String, String>> verifyEmail(@PathVariable String token) {
		Map<String, String> response = new HashMap<>();
		try {
			// Verify the user's email using the token
			User verifiedUser = userService.verifyUserEmail(token);
			response.put("message", "Email verification successful for user: " + verifiedUser.getUsername());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "Email verification failed: Invalid verification token.");
			return ResponseEntity.badRequest().body(response);
		}
	}

}
