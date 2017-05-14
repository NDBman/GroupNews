package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.converter.GroupConverter;
import com.epam.internship.converter.MembershipConverter;
import com.epam.internship.converter.UserConverter;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.Membership;
import com.epam.internship.dto.User;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.Role;

public class MembershipConverterTest {

	private MembershipConverter membershipConverter = new MembershipConverter();

	private UserConverter userConverter = new UserConverter();

	private GroupConverter groupConverter = new GroupConverter();

	private final Long ID = 1L;

	@Test
	public void shouldReturnMembershipEntityWithGivenFields() {
		// Given
		User user = User.builder().id(ID).build();
		Group group = Group.builder().id(ID).createdBy(user).build();
		Membership membership = Membership.builder().id(ID).member(user).group(group).role(Role.ADMIN).build();

		// When
		MembershipEntity membershipEntity = membershipConverter.convert(membership);

		// Then
		assertEquals(membership.getId(), membershipEntity.getId());
		assertEquals(userConverter.convert(membership.getMember()), membershipEntity.getMember());
		assertEquals(groupConverter.convert(membership.getGroup()), membershipEntity.getGroupEntity());
		assertEquals(membership.getRole(), membershipEntity.getRole());
	}
}
