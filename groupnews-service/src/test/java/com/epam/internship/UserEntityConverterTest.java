package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.converter.UserEntityConverter;
import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;

public class UserEntityConverterTest {

	private final Long USER_ID = 2L;
	private final String USER_NAME = "Mr. Brown";
	private final String USER_EMAIL = "brown@email.com";

	private UserEntityConverter systemUnderTest = new UserEntityConverter();

	@Test
	public void shouldReturnUserEntityWithSameFieldValues() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		// When
		User user = systemUnderTest.convert(userEntity);
		// Then
		assertEquals(USER_ID, user.getId());
		assertEquals(USER_NAME, user.getName());
		assertEquals(USER_EMAIL, user.getEmail());
	}
}
