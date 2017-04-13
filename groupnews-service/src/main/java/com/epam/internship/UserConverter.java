package com.epam.internship;

import java.util.ArrayList;
import java.util.List;

import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;

public class UserConverter {

	public User convertUserEntity(UserEntity userEntity){
		return User.builder().name(userEntity.getName()).email(userEntity.getEmail()).build();
	}
	
	public List<User> convertUserEntities(List<UserEntity> userEntities){
		List<User> users = new ArrayList<>();
		for(UserEntity userEntity : userEntities){
			users.add(convertUserEntity(userEntity));
		}
		return users;
	}
	
	public UserEntity convertUser(User user){
		return UserEntity.builder().name(user.getName()).email(user.getEmail()).build();
	}
}
