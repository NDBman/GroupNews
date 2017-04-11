package com.epam.internship.impl;

import org.springframework.stereotype.Service;

import com.epam.internship.UserService;
import com.epam.internship.dto.User;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User createUser(String name, String email) {
		return User.builder().name(name).email(email).build();
	}

}
