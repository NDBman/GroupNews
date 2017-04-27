package com.epam.internship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.epam.internship.entity.UserEntity;

public class UserEntityIT {

	private static final Long USER_ID = 1L;
	private static final String USER_NAME = "Joe";
	private static final String USER_EMAIL = "alma@email.hu";

	private static final Long USER_ID_2 = 2L;

	private static final UserEntity userEntity1 = new UserEntity(USER_ID, USER_NAME, USER_EMAIL);

	@Test
	public void shouldMatchWithTheGivenValuesInConstructor() {
		// When
		UserEntity userEntity = new UserEntity(USER_ID, USER_NAME, USER_EMAIL);
		// Then
		assertEquals(USER_ID.longValue(), userEntity.getId().longValue());
		assertEquals(USER_NAME, userEntity.getName());
		assertEquals(USER_EMAIL, userEntity.getEmail());
	}

	@Test
	public void shouldMatchWithGivenValuesInSetters() {
		// Given
		UserEntity userEntity = new UserEntity();
		// When
		userEntity.setId(USER_ID);
		userEntity.setName(USER_NAME);
		userEntity.setEmail(USER_EMAIL);
		// Then
		assertEquals(USER_ID.longValue(), userEntity.getId().longValue());
		assertEquals(USER_NAME, userEntity.getName());
		assertEquals(USER_EMAIL, userEntity.getEmail());
	}

	@Test
	public void shouldNotEqualWhenOtherValuesAreGiven() {
		// Given
		UserEntity userEntity = new UserEntity(USER_ID_2, USER_NAME, USER_EMAIL);
		// When Then
		assertFalse(userEntity1.equals(userEntity) && userEntity.equals(userEntity1));
	}

}
