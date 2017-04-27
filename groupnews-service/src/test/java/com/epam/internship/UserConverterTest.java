package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.converter.UserConverter;
import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;

public class UserConverterTest {

	private final String USER_NAME = "Mr. Brown";
	private final String USER_EMAIL = "brown@email.com";

	private UserConverter systemUnderTest = new UserConverter();

	@Test
	public void shouldReturnUserEntityWithSameFieldValues() {
		// Given
		User user = User.builder().name(USER_NAME).email(USER_EMAIL).build();
		// When
		UserEntity userEntity = systemUnderTest.convert(user);
		// Then
		assertEquals(USER_NAME, userEntity.getName());
		assertEquals(USER_EMAIL, userEntity.getEmail());
	}
}
