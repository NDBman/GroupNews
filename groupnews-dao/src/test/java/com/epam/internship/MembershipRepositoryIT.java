package com.epam.internship;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.junit4.SpringRunner;

import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.Role;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.MembershipRepository;
import com.epam.internship.repo.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootApplication
@Transactional
public class MembershipRepositoryIT {

	@Autowired
	private MembershipRepository systemUnderTest;

	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private UserRepository userRepository;

	private MembershipEntity membershipEntity;

	private Long ID = 1L;

	@Test
	public void shouldReturnOneWhenCountIsCalled() {
		assertEquals(1, systemUnderTest.count());
	}

	@Test
	public void shouldReturnSameMembershipWhenSaveIsCalled() {
		// Given
		membershipEntity = MembershipEntity.builder().id(ID).member(userRepository.findOne(ID))
				.groupEntity(groupRepository.findOne(ID)).role(Role.USER).build();
		// When
		MembershipEntity savedMembershipEntity = systemUnderTest.save(membershipEntity);
		// Then
		assertEquals(membershipEntity, savedMembershipEntity);
	}

	@Test
	public void shouldReturnMembershipWithSameIdWhenFindOneIsCalled() {
		// Given
		membershipEntity = MembershipEntity.builder().id(ID).build();
		// When
		Long actualId = systemUnderTest.findOne(ID).getId();
		// Then
		assertEquals(ID, actualId);
	}

	@Test
	public void shouldReturnAllMembershipsWhenFindAllIsCalled() {
		// Given
		membershipEntity = MembershipEntity.builder().id(2L).build();
		systemUnderTest.save(membershipEntity);
		// When
		List<MembershipEntity> memberships = systemUnderTest.findAll();
		// Then
		assertEquals(2, memberships.size());
	}

	@Test
	public void shouldDeleteMembershipWhenDeleteIsCalledWithIdParam() {
		// When
		systemUnderTest.delete(ID);
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test
	public void shouldDeleteMembershipWhenDeleteIsCalledWithEntity() {
		// Given
		membershipEntity = MembershipEntity.builder().id(ID).build();
		// When
		systemUnderTest.delete(membershipEntity);
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test
	public void shouldDeleteAllMembershipWhenDeleteAllIsCalled() {
		// Given
		membershipEntity = MembershipEntity.builder().id(2L).build();
		systemUnderTest.save(membershipEntity);
		// When
		systemUnderTest.deleteAll();
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test
	public void shouldReturnListOfMembershipsBelongingToGivenUser() {
		// Given
		UserEntity userEntity = userRepository.findOne(ID);
		GroupEntity groupEntity = groupRepository.findOne(ID);
		membershipEntity = MembershipEntity.builder().member(userEntity).groupEntity(groupEntity).build();
		MembershipEntity membershipEntity2 = MembershipEntity.builder().member(userEntity).groupEntity(groupEntity)
				.role(Role.USER).build();
		systemUnderTest.save(membershipEntity2);
		systemUnderTest.save(membershipEntity);
		List<MembershipEntity> expectedMemberships = Arrays.asList(membershipEntity2, membershipEntity);
		// When
		List<MembershipEntity> actualMemberships = systemUnderTest.findByMember(userEntity);
		// Then
		assertEquals(expectedMemberships, actualMemberships);
	}
}
