package com.epam.internship;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.UserRepository;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@Transactional
public class GroupRepositoryIT {

	@Autowired
	private GroupRepository systemUnderTest;

	@Autowired
	private UserRepository userRepository;
	private final Long USER_ID = 1L;

	private static GroupEntity groupEntity2;

	private static final Long GROUP_ID_1 = 1L;

	private static final String GROUP_TITLE_1 = "Group 1";
	private static final String GROUP_DESCRIPTION_1 = "Group description 1";

	private static final String GROUP_TITLE_2 = "Group 1";
	private static final String GROUP_DESCRIPTION_2 = "Group description 2";

	@BeforeClass
	public static void setUp() {
		// Given
		groupEntity2 = GroupEntity.builder().title(GROUP_TITLE_2).description(GROUP_DESCRIPTION_2).build();
	}

	@Test
	public void shouldReturnOneWhenCountIsCalledAfterSavingOneGroup() {
		// WhenThen
		assertEquals(1, systemUnderTest.count());
	}

	@Test
	public void shouldReturnGroupWithSameFieldsWhenSaveIsCalled() {
		// Given
		UserEntity userEntity = userRepository.findOne(USER_ID);
		groupEntity2.setCreatedBy(userEntity);
		// When
		GroupEntity savedGroupEntity = systemUnderTest.save(groupEntity2);
		// Then
		assertEquals(GROUP_TITLE_2, savedGroupEntity.getTitle());
		assertEquals(userEntity, savedGroupEntity.getCreatedBy());
		assertEquals(GROUP_DESCRIPTION_2, savedGroupEntity.getDescription());
	}

	@Test
	public void shouldReturnGroupWithSameIdWhenFindOneIsCalled() {
		// When
		GroupEntity foundGroupEntity = systemUnderTest.findOne(GROUP_ID_1);
		// Then
		assertEquals(GROUP_ID_1, foundGroupEntity.getId());
	}

	@Test
	public void shouldReturnTwoGroupsWhenFindAllIsCalled() {
		// Given
		systemUnderTest.save(groupEntity2);
		// When
		List<GroupEntity> groupEntites = systemUnderTest.findAll();
		// Then
		assertEquals(2, groupEntites.size());
	}

	@Test
	public void shouldDeleteGroupWithGivenIdWhenDeleteWithIdParamIsCalled() {
		// When
		systemUnderTest.delete(GROUP_ID_1);
		// Then
		assertEquals(null, systemUnderTest.findOne(GROUP_ID_1));
	}

	@Test
	public void shouldDeleteGivenGroupWhenDeleteWithGroupEntityParamIsCalled() {
		// Given
		GroupEntity savedGroupEntity = systemUnderTest.save(groupEntity2);
		// When
		systemUnderTest.delete(savedGroupEntity);
		// Then
		assertEquals(null, systemUnderTest.findOne(savedGroupEntity.getId()));
	}

	// Cannot delete every group entity because some of them are referenced in
	// other entities.
	@Test(expected = DataIntegrityViolationException.class)
	public void shouldCountEqualToZeroWhenDeleteAllIsCalled() {
		// Given
		systemUnderTest.save(groupEntity2);
		// When
		systemUnderTest.deleteAll();
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test
	public void shouldReturnListOfGroupsBelongingToGivenUserWhenFindByCreatedByIsCalled() {
		// Given
		UserEntity userEntity = userRepository.findOne(USER_ID);
		GroupEntity groupEntity1 = systemUnderTest.findOne(GROUP_ID_1);
		GroupEntity groupEntity2 = GroupEntity.builder().title(GROUP_TITLE_1).createdBy(userEntity)
				.description(GROUP_DESCRIPTION_1).build();

		systemUnderTest.save(groupEntity2);
		List<GroupEntity> expectedList = Arrays.asList(groupEntity1, groupEntity2);
		// When
		List<GroupEntity> usersCreatedGroups = systemUnderTest.findByCreatedBy(userEntity);
		// Then
		assertEquals(expectedList, usersCreatedGroups);
	}
}
