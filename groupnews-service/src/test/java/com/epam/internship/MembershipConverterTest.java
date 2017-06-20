package com.epam.internship;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.internship.converter.GroupConverter;
import com.epam.internship.converter.MembershipConverter;
import com.epam.internship.converter.UserConverter;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.Membership;
import com.epam.internship.dto.User;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.UserEntity;

@RunWith(MockitoJUnitRunner.class)
public class MembershipConverterTest {

	@InjectMocks
	private MembershipConverter systemUnderTest;

	@Mock
	private UserConverter userConverter;

	@Mock
	private GroupConverter groupConverter;

	private final Long MEMBERSHIP_ID = 7L;

	private final String ROLE_USER = "USER";
	private final String ROLE_ADMIN = "ADMIN";

	private final Long USER_ID = 1L;
	private final String USER_NAME = "Tim";
	private final String USER_EMAIL = "email@test.hu";

	private final Long GROUP_ID = 5L;
	private final String GROUP_TITLE = "Group";
	private final String GROUP_DESCRIPTION = "Description";

	@Test
	public void shouldReturnMembershipEntityWithGivenFieldsAndAdminRole() {
		// Given
		User user = User.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		Group group = Group.builder().id(GROUP_ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION).createdBy(user)
				.build();
		Membership membership = Membership.builder().id(MEMBERSHIP_ID).member(user).group(group).role(ROLE_ADMIN)
				.build();

		UserEntity userEntity = UserEntity.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.build();

		when(userConverter.convert(user)).thenReturn(userEntity);
		when(groupConverter.convert(group)).thenReturn(groupEntity);
		// When
		MembershipEntity membershipEntity = systemUnderTest.convert(membership);

		// Then
		assertEquals(membership.getId(), membershipEntity.getId());
		assertEquals(userConverter.convert(membership.getMember()), membershipEntity.getMember());
		assertEquals(groupConverter.convert(membership.getGroup()), membershipEntity.getGroup());
		assertEquals(membership.getRole(), membershipEntity.getRole().toString());
	}

	@Test
	public void shouldReturnMembershipEntityWithGivenFieldsAndUserRole() {
		// Given
		User user = User.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		Group group = Group.builder().id(GROUP_ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION).createdBy(user)
				.build();
		Membership membership = Membership.builder().id(MEMBERSHIP_ID).member(user).group(group).role(ROLE_USER)
				.build();

		UserEntity userEntity = UserEntity.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.build();

		when(userConverter.convert(user)).thenReturn(userEntity);
		when(groupConverter.convert(group)).thenReturn(groupEntity);
		// When
		MembershipEntity membershipEntity = systemUnderTest.convert(membership);

		// Then
		assertEquals(membership.getId(), membershipEntity.getId());
		assertEquals(userConverter.convert(membership.getMember()), membershipEntity.getMember());
		assertEquals(groupConverter.convert(membership.getGroup()), membershipEntity.getGroup());
		assertEquals(membership.getRole(), membershipEntity.getRole().toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWhenRoleDoesNotExists() {
		// Given
		User user = User.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		Group group = Group.builder().id(GROUP_ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION).createdBy(user)
				.build();
		Membership membership = Membership.builder().id(MEMBERSHIP_ID).member(user).group(group)
				.role("not existing role").build();

		UserEntity userEntity = UserEntity.builder().id(USER_ID).name(USER_NAME).email(USER_EMAIL).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.build();

		when(userConverter.convert(user)).thenReturn(userEntity);
		when(groupConverter.convert(group)).thenReturn(groupEntity);
		// When
		MembershipEntity membershipEntity = systemUnderTest.convert(membership);

		// Then
		assertEquals(membership.getId(), membershipEntity.getId());
		assertEquals(userConverter.convert(membership.getMember()), membershipEntity.getMember());
		assertEquals(groupConverter.convert(membership.getGroup()), membershipEntity.getGroup());
		assertEquals(membership.getRole(), membershipEntity.getRole().toString());
	}
}
