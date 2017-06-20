package com.epam.internship;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.internship.converter.GroupEntityConverter;
import com.epam.internship.converter.UserEntityConverter;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.User;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.UserEntity;

@RunWith(MockitoJUnitRunner.class)
public class GroupEntityConverterTest {

	@InjectMocks
	private GroupEntityConverter systemUnderTest;

	@Mock
	private UserEntityConverter userEntityConverter;

	private final Long GROUP_ID = 10L;
	private final String TITLE = "Title";
	private final String DESCRIPTION = "Desc";

	private final Long USER_ID = 1L;
	private final String USER_NAME = "Tim";
	private final String USER_EMAIL = "email@test.hu";

	@Test
	public void shouldReturnGroupDtoWithMatchingFields() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).title(TITLE).description(DESCRIPTION)
				.createdBy(userEntity).build();

		User user = User.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();

		when(userEntityConverter.convert(userEntity)).thenReturn(user);
		// When
		Group group = systemUnderTest.convert(groupEntity);
		// Then
		assertEquals(GROUP_ID, group.getId());
		assertEquals(TITLE, group.getTitle());
		assertEquals(DESCRIPTION, group.getDescription());
		assertEquals(userEntityConverter.convert(userEntity), group.getCreatedBy());
	}
}
