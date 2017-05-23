package com.epam.internship;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.internship.converter.GroupConverter;
import com.epam.internship.converter.UserConverter;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.User;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.UserEntity;

@RunWith(MockitoJUnitRunner.class)
public class GroupConverterTest {

	@InjectMocks
	private GroupConverter systemUnderTest;

	@Mock
	private UserConverter userConverter;

	private final Long GROUP_ID = 1L;
	private final String TITLE = "Titlte";
	private final String DESCRIPTION = "Description";

	private final Long USER_ID = 1L;
	private final String USER_NAME = "Tim";
	private final String USER_EMAIL = "email@test.hu";

	@Test
	public void shouldReturnGroupEntityWithMatchingFields() {
		// Given
		User user = User.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		Group group = Group.builder().id(GROUP_ID).title(TITLE).description(DESCRIPTION).createdBy(user).build();

		UserEntity userEntity = UserEntity.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		when(userConverter.convert(user)).thenReturn(userEntity);
		// When
		GroupEntity groupEntity = systemUnderTest.convert(group);
		// Then
		assertEquals(GROUP_ID, groupEntity.getId());
		assertEquals(TITLE, groupEntity.getTitle());
		assertEquals(DESCRIPTION, groupEntity.getDescription());
		assertEquals(userConverter.convert(user), groupEntity.getCreatedBy());
	}
}
