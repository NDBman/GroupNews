package com.epam.internship;

import java.util.List;

import com.epam.internship.dto.User;

public interface UserService {

	User createUser(String name, String email);

	List<User> getAllUsers();

	boolean emailAlreadyExists(String email);

}
