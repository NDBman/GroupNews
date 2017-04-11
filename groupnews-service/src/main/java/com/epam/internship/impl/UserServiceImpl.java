package com.epam.internship.impl;

import org.springframework.stereotype.Service;

import com.epam.internship.UserService;
import com.epam.internship.entity.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public UserEntity createUser(String name, String email) {
		return UserEntity.builder().name(name).email(email).build();
	}

}
