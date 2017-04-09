package com.epam.internship.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epam.internship.entity.UserEntity;

@RestController
@RequestMapping("/users")
public class UserController {

	private List<UserEntity> users = new ArrayList<>();
	
	public UserController() {
		users.add(UserEntity.builder().name("Mr. Brown").email("brown@test.com").build());
		users.add(UserEntity.builder().name("Mr. Green").email("green@test.com").build());
	}
	
	@RequestMapping(method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<UserEntity> getUsers(){
		return users;
	}
}
