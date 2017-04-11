package com.epam.internship;

import com.epam.internship.entity.UserEntity;

public interface UserService {

	public UserEntity createUser(String name, String email);
}
