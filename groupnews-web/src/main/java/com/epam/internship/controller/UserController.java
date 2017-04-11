package com.epam.internship.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.internship.UserService;
import com.epam.internship.dto.User;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<User> getUsers() {
		return userService.getAllUsers();
	}
}
