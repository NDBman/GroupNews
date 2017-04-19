package com.epam.internship.converter;

import org.springframework.core.convert.converter.Converter;

import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;

public class UserConverter implements Converter<User, UserEntity> {

	@Override
	public UserEntity convert(User source) {
		return UserEntity.builder().name(source.getName()).email(source.getEmail()).build();
	}

}
