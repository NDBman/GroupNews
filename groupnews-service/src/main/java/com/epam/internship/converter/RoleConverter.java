package com.epam.internship.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.epam.internship.entity.Role;
import com.epam.internship.exception.RoleDoesNotExistsException;

@Service
public class RoleConverter implements Converter<String, Role> {

	@Override
	public Role convert(String source) {
		switch (source) {
		case "ADMIN":
			return Role.ADMIN;
		case "USER":
			return Role.USER;
		default:
			throw new RoleDoesNotExistsException();
		}
	}

}
