package com.epam.internship.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
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

	@Override
	public boolean emailAlreadyExists(String email) {
		for(UserEntity userEntity : userRepository.findAll()){
			if(userEntity.getEmail().equals(email))
				return true;
		}
		return false;
	}

	@Override
	public boolean isEmailValid(String email) {
		Matcher matcher = pattern.matcher(email);
		if(matcher.find()){
			return false;
		}
		return true;
	}

}
