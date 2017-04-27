package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.entity.UserEntity;

public class UserEntityBuilderIT {

	private static Long USER_ID = 1L;
	
	@Test
	public void shouldBuildUserEntityWithGivenId(){
		UserEntity userEntity = UserEntity.builder().id(USER_ID).build();
		assertEquals(USER_ID, userEntity.getId());
	}
	
	@Test
	public void toStringsShouldEqual(){
		assertEquals(UserEntity.builder().toString(), UserEntity.builder().toString());
	}
}
