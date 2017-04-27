package com.epam.internship;

import java.util.List;

import com.epam.internship.dto.User;

import javassist.NotFoundException;

public interface UserService {


	User createUser(String name, String email);

	List<User> getAllUsers();

	User getUserById(Long id) throws NotFoundException;

}
