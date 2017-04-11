package com.epam.internship.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.epam.internship.UserService;
import com.epam.internship.dto.User;

@Service
public class UserServiceImpl implements UserService {
	
	private List<User> users = new ArrayList<>();
	
	public UserServiceImpl() {
		users.add(User.builder().name("Mr. Brown").email("brown@email.com").build());
	}

	@Override
	public User createUser(String name, String email) {
		User newUser = User.builder().name(name).email(email).build();
		users.add(newUser);
		return newUser;
	}

	@Override
	public List<User> getAllUsers() {
		return users;
	}

}
