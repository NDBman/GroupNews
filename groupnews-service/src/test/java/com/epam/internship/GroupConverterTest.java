package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.converter.GroupConverter;
import com.epam.internship.converter.UserConverter;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.User;
import com.epam.internship.entity.GroupEntity;

public class GroupConverterTest {

	private GroupConverter systemUnderTest = new GroupConverter();
	
	private UserConverter userConverter = new UserConverter();
	
	private final Long ID = 1L;
	private final String TITLE = "Titlte";
	private final String DESCRIPTION = "Description";
	
	@Test
	public void shouldReturnGroupEntityWithMatchingFields(){
		User user = new User();
		Group group = Group.builder().id(1L).title(TITLE).description(DESCRIPTION).createdBy(user).build();
		GroupEntity groupEntity = systemUnderTest.convert(group);
		assertEquals(ID, groupEntity.getId());
		assertEquals(TITLE, groupEntity.getTitle());
		assertEquals(DESCRIPTION, groupEntity.getDescription());
		assertEquals(userConverter.convert(user), groupEntity.getCreatedBy());
	}
}
