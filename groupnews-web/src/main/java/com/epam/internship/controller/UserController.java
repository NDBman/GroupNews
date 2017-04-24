package com.epam.internship.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.internship.UserService;
import com.epam.internship.dto.User;

@RestController
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("users")
	public List<User> getUsers() {
		return userService.getAllUsers();
	}

	@PostMapping(value = "users/new")
	public ResponseEntity<User> registerUser(@RequestParam String name, @RequestParam String email) {
		return new ResponseEntity<User>(userService.createUser(name, email), HttpStatus.OK);
	}
}
