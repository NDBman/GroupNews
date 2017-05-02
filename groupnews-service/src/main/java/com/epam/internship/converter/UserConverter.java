package com.epam.internship.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;

@Service
public class UserConverter implements Converter<User, UserEntity> {

	@Override
	public UserEntity convert(User source) {
		return UserEntity.builder().id(source.getId()).name(source.getName()).email(source.getEmail()).build();
	}

}
