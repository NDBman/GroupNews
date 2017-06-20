package com.epam.internship;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.Role;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.MembershipRepository;
import com.epam.internship.repo.UserRepository;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@Transactional
public class MembershipRepositoryIT {

	@Autowired
	private MembershipRepository systemUnderTest;

	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private UserRepository userRepository;

	private MembershipEntity membershipEntity;

	private final Long MEMBERSHIP_ID_1 = 1L;
	private final Long MEMBERSHIP_ID_2 = 2L;

	private final Long USER_ID = 1L;
	private final String USER_NAME = "Tim";
	private final String USER_EMAIL = "email@test.hu";

	private final Long GROUP_ID = 1L;
	private final String GROUP_TITLE = "Title";
	private final String GROUP_DESCRIPTION = "description";

	@Test
	public void shouldReturnOneWhenCountIsCalled() {
		assertEquals(1, systemUnderTest.count());
	}

	@Test
	public void shouldReturnSameMembershipWhenSaveIsCalled() {
		// Given
		membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID_1).member(userRepository.findOne(USER_ID))
				.group(groupRepository.findOne(GROUP_ID)).role(Role.USER).build();
		// When
		MembershipEntity savedMembershipEntity = systemUnderTest.save(membershipEntity);
		// Then
		assertEquals(membershipEntity, savedMembershipEntity);
	}

	@Test
	public void shouldReturnMembershipWithSameIdWhenFindOneIsCalled() {
		// Given
		membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID_1).build();
		// When
		Long actualId = systemUnderTest.findOne(MEMBERSHIP_ID_1).getId();
		// Then
		assertEquals(MEMBERSHIP_ID_1, actualId);
	}

	@Test
	public void shouldReturnAllMembershipsWhenFindAllIsCalled() {
		// Given
		UserEntity userEntity = UserEntity.builder().name(USER_NAME).email(USER_EMAIL).build();
		membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID_2).member(userEntity)
				.group(groupRepository.findOne(GROUP_ID)).role(Role.USER).build();
		userRepository.save(userEntity);
		systemUnderTest.save(membershipEntity);
		// When
		List<MembershipEntity> memberships = systemUnderTest.findAll();
		// Then
		assertEquals(2, memberships.size());
	}

	@Test
	public void shouldDeleteMembershipWhenDeleteIsCalledWithIdParam() {
		// When
		systemUnderTest.delete(MEMBERSHIP_ID_1);
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test
	public void shouldDeleteMembershipWhenDeleteIsCalledWithEntity() {
		// Given
		membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID_1).build();
		// When
		systemUnderTest.delete(membershipEntity);
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test
	public void shouldDeleteAllMembershipWhenDeleteAllIsCalled() {
		// Given
		UserEntity userEntity = UserEntity.builder().name(USER_NAME).email(USER_EMAIL).build();
		GroupEntity groupEntity = GroupEntity.builder().title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.createdBy(userRepository.findOne(USER_ID)).build();
		membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID_2).member(userEntity).group(groupEntity)
				.role(Role.USER).build();
		userRepository.save(userEntity);
		groupRepository.save(groupEntity);
		systemUnderTest.save(membershipEntity);
		// When
		systemUnderTest.deleteAll();
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test
	public void shouldReturnListOfMembershipsBelongingToGivenUser() {
		// Given
		UserEntity userEntity = userRepository.findOne(USER_ID);
		GroupEntity groupEntity = GroupEntity.builder().title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.createdBy(userEntity).build();
		MembershipEntity membershipEntity1 = systemUnderTest.findOne(MEMBERSHIP_ID_1);
		MembershipEntity membershipEntity2 = MembershipEntity.builder().member(userEntity).group(groupEntity)
				.role(Role.USER).build();

		groupRepository.save(groupEntity);
		systemUnderTest.save(membershipEntity2);
		List<MembershipEntity> expectedMemberships = Arrays.asList(membershipEntity1, membershipEntity2);
		// When
		List<MembershipEntity> actualMemberships = systemUnderTest.findByMember(userEntity);
		// Then
		assertEquals(expectedMemberships, actualMemberships);
	}

	@Test
	public void shouldReturnListOfMembershipsBelongingToGivenGroup() {
		// Given
		UserEntity userEntity = UserEntity.builder().name(USER_NAME).email(USER_EMAIL).build();
		GroupEntity groupEntity = groupRepository.findOne(GROUP_ID);
		MembershipEntity membershipEntity1 = systemUnderTest.findOne(MEMBERSHIP_ID_1);
		MembershipEntity membershipEntity2 = MembershipEntity.builder().member(userEntity).group(groupEntity)
				.role(Role.USER).build();
		userRepository.save(userEntity);
		groupRepository.save(groupEntity);
		systemUnderTest.save(membershipEntity2);
		List<MembershipEntity> expectedMembershipEntites = Arrays.asList(membershipEntity1, membershipEntity2);
		// When
		List<MembershipEntity> actualMembershipEntites = systemUnderTest.findByGroup(groupEntity);
		// Then
		assertEquals(expectedMembershipEntites, actualMembershipEntites);
	}

	@Test
	public void shouldReturnMemberShipBelongingToGivenUserAndGroup() {
		// Given
		UserEntity userEntity = userRepository.findOne(USER_ID);
		GroupEntity groupEntity = groupRepository.findOne(GROUP_ID);
		MembershipEntity membershipEntity = systemUnderTest.findOne(MEMBERSHIP_ID_1);
		// When
		MembershipEntity actualMembershipEntity = systemUnderTest.findByMemberAndGroup(userEntity, groupEntity);
		// Then
		assertEquals(membershipEntity, actualMembershipEntity);
	}
}
