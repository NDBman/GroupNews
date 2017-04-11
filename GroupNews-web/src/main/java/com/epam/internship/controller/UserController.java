package com.epam.internship.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epam.internship.UserService;
import com.epam.internship.entity.UserEntity;

@RestController
@RequestMapping("/users")
public class UserController {

	private List<UserEntity> users = new ArrayList<>();
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<UserEntity> getUsers(){
		users.add(userService.createUser("Mr. Brown", "brown@test.com"));
		users.add(userService.createUser("Mr. Green", "green@test.com"));
		return users;
	}
}
