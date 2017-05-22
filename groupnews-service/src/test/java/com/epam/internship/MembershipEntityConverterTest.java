package com.epam.internship;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.internship.converter.GroupEntityConverter;
import com.epam.internship.converter.MembershipEntityConverter;
import com.epam.internship.converter.UserEntityConverter;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.Membership;
import com.epam.internship.dto.User;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.Role;
import com.epam.internship.entity.UserEntity;

@RunWith(MockitoJUnitRunner.class)
public class MembershipEntityConverterTest {

	@InjectMocks
	private MembershipEntityConverter systemUnderTest;

	@Mock
	private UserEntityConverter userEntityConverter;

	@Mock
	private GroupEntityConverter groupEntityConverter;

	private final Long ID = 1L;

	private final String USER_NAME = "Tim";
	private final String USER_EMAIL = "email@test.hu";

	private final String GROUP_TITLE = "Group";
	private final String GROUP_DESCRIPTION = "Description";

	@Test
	public void shouldReturnMembershipDtoWithGivenFields() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(ID).name(USER_NAME).email(USER_EMAIL).build();
		GroupEntity groupEntity = GroupEntity.builder().id(ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.createdBy(userEntity).build();
		MembershipEntity membershipEntity = MembershipEntity.builder().id(ID).member(userEntity).group(groupEntity)
				.role(Role.ADMIN).build();
		User user = User.builder().id(ID).name(USER_NAME).email(USER_EMAIL).build();
		Group group = Group.builder().id(ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION).createdBy(user).build();
		when(userEntityConverter.convert(userEntity)).thenReturn(user);
		when(groupEntityConverter.convert(groupEntity)).thenReturn(group);
		// When
		Membership membership = systemUnderTest.convert(membershipEntity);
		// Then
		assertEquals(membershipEntity.getId(), membership.getId());
		assertEquals(userEntityConverter.convert(membershipEntity.getMember()), membership.getMember());
		assertEquals(groupEntityConverter.convert(membershipEntity.getGroup()), membership.getGroup());
		assertEquals(membershipEntity.getRole(), membership.getRole());
	}
}
