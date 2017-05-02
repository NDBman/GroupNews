package com.epam.internship.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;

@Service
public class UserEntityConverter implements Converter<UserEntity, User> {

	@Override
	public User convert(UserEntity source) {
		return User.builder().id(source.getId()).name(source.getName()).email(source.getEmail()).build();
	}

}
