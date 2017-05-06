package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.converter.GroupEntityConverter;
import com.epam.internship.converter.UserEntityConverter;
import com.epam.internship.dto.Group;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.UserEntity;

public class GroupEntityConverterTest {

	private GroupEntityConverter systemUnderTest = new GroupEntityConverter();
	
	private UserEntityConverter userEntityConverter = new UserEntityConverter();
	
	private final Long ID = 1L;
	private final String TITLE = "Title";
	private final String DESCRIPTION = "Desc";
	
	@Test
	public void shouldReturnGroupDtoWithMatchingFields(){
		UserEntity userEntity = new UserEntity();
		GroupEntity groupEntity = GroupEntity.builder().id(ID).title(TITLE).description(DESCRIPTION).createdBy(userEntity).build();
		Group group = systemUnderTest.convert(groupEntity);
		assertEquals(ID, group.getId());
		assertEquals(TITLE, group.getTitle());
		assertEquals(DESCRIPTION, group.getDescription());
		assertEquals(userEntityConverter.convert(userEntity), group.getCreatedBy());
	}
}
