package com.epam.internship;

import java.util.List;

import com.epam.internship.dto.User;

public interface UserService {

	public User createUser(String name, String email);
	public List<User> getAllUsers();
	public boolean emailAlreadyExists(String email);
	public boolean isEmailValid(String email);
}
