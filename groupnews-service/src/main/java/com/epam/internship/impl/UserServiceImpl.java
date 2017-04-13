package com.epam.internship.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.internship.UserConverter;
import com.epam.internship.UserService;
import com.epam.internship.dto.User;
import com.epam.internship.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private UserConverter userConverter = new UserConverter();

	@Override
	public User createUser(String name, String email) {
		User newUser = User.builder().name(name).email(email).build();
		userRepository.save(userConverter.convertUser(newUser));
		return newUser;
	}

	@Override
	public List<User> getAllUsers() {
		return userConverter.convertUserEntities(userRepository.findAll());
		
	}

}
