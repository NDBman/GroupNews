package com.epam.internship.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import com.epam.internship.UserService;
import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConversionService conversionService;

	@Override
	public User createUser(String name, String email) {
		User newUser = User.builder().name(name).email(email).build();
		userRepository.save(conversionService.convert(newUser, UserEntity.class));
		return newUser;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		return (List<User>) conversionService.convert(userRepository.findAll(),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserEntity.class)),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(User.class)));

	}

}
