package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.converter.GroupEntityConverter;
import com.epam.internship.converter.MembershipEntityConverter;
import com.epam.internship.converter.UserEntityConverter;
import com.epam.internship.dto.Membership;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.Role;
import com.epam.internship.entity.UserEntity;

public class MembershipEntityConverterTest {

	private MembershipEntityConverter systemUnderTest = new MembershipEntityConverter();

	private UserEntityConverter userEntityConverter = new UserEntityConverter();

	private GroupEntityConverter groupEntityConverter = new GroupEntityConverter();

	private final Long ID = 1L;

	@Test
	public void shouldReturnMembershipDtoWithGivenFields() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(ID).build();
		GroupEntity groupEntity = GroupEntity.builder().id(ID).createdBy(userEntity).build();
		MembershipEntity membershipEntity = MembershipEntity.builder().id(ID).member(userEntity)
				.groupEntity(groupEntity).role(Role.ADMIN).build();

		// When
		Membership membership = systemUnderTest.convert(membershipEntity);
		// Then
		assertEquals(membershipEntity.getId(), membership.getId());
		assertEquals(userEntityConverter.convert(membershipEntity.getMember()), membership.getMember());
		assertEquals(groupEntityConverter.convert(membershipEntity.getGroupEntity()), membership.getGroup());
		assertEquals(membershipEntity.getRole(), membership.getRole());
	}
}
