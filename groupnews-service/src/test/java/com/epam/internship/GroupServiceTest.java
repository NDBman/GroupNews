package com.epam.internship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import com.epam.internship.dto.Group;
import com.epam.internship.dto.User;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.exception.UserDoesNotExistsException;
import com.epam.internship.impl.GroupServiceImpl;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private ConversionService conversionService;

	@Mock
	private GroupRepository groupRepository;

	@InjectMocks
	private GroupServiceImpl systemUnderTest;

	@Captor
	private ArgumentCaptor<GroupEntity> captor;

	private final String GROUP_TITLE = "Group Title";
	private final String GROUP_DESCRIPTION = "Group description";
	private final Long USER_ID = 1L;

	private final String GROUP_TITLE_2 = "Group title 2";
	private final String GROUP_DESCRIPTION_2 = "Group desc 2";

	private final Long GROUP_ID_1 = 1L;
	private final Long GROUP_ID_2 = 2L;

	@Test
	public void shouldCreateGroupWithGivenFields() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID).build();
		GroupEntity groupEntity = GroupEntity.builder().title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.createdBy(userEntity).build();
		when(userRepository.findOne(USER_ID)).thenReturn(userEntity);
		when(conversionService.convert(groupEntity, Group.class)).thenReturn(new Group());
		// When
		systemUnderTest.createGoup(USER_ID, GROUP_TITLE, GROUP_DESCRIPTION);
		verify(groupRepository).save(captor.capture());
		// Then
		assertEquals(GROUP_TITLE, captor.getValue().getTitle());
		assertEquals(GROUP_DESCRIPTION, captor.getValue().getDescription());
		assertEquals(userEntity, captor.getValue().getCreatedBy());
	}

	@Test(expected = UserDoesNotExistsException.class)
	public void shouldFailWhenUserDoesNotExist() {
		systemUnderTest.createGoup(USER_ID, GROUP_TITLE, GROUP_DESCRIPTION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenTitleIsAnEmptyString() {
		when(userRepository.findOne(USER_ID)).thenReturn(new UserEntity());
		systemUnderTest.createGoup(USER_ID, "", GROUP_DESCRIPTION);
	}

	@Test(expected = NullPointerException.class)
	public void shouldFailWhenTitleIsNull() {
		when(userRepository.findOne(USER_ID)).thenReturn(new UserEntity());
		systemUnderTest.createGoup(USER_ID, null, GROUP_DESCRIPTION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenTitleCharacterNumberExceeds70() {
		when(userRepository.findOne(USER_ID)).thenReturn(new UserEntity());
		systemUnderTest.createGoup(USER_ID, RandomStringUtils.random(71), GROUP_DESCRIPTION);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenDescCharacterNumberExceeds2000() {
		when(userRepository.findOne(USER_ID)).thenReturn(new UserEntity());
		systemUnderTest.createGoup(USER_ID, GROUP_TITLE, RandomStringUtils.random(2001));
	}

	@Test
	public void shouldReturnListOfGroupsBelongingToUser() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID).build();
		GroupEntity groupEntity1 = GroupEntity.builder().id(GROUP_ID_1).title(GROUP_TITLE)
				.description(GROUP_DESCRIPTION).createdBy(userEntity).build();
		GroupEntity groupEntity2 = GroupEntity.builder().id(GROUP_ID_2).title(GROUP_TITLE_2)
				.description(GROUP_DESCRIPTION_2).createdBy(userEntity).build();
		User user = User.builder().id(USER_ID).build();
		Group group1 = Group.builder().id(GROUP_ID_1).title(GROUP_TITLE).description(GROUP_DESCRIPTION).createdBy(user)
				.build();
		Group group2 = Group.builder().id(GROUP_ID_2).title(GROUP_TITLE_2).description(GROUP_DESCRIPTION_2)
				.createdBy(user).build();
		List<Group> expectedGroups = new ArrayList<>();
		expectedGroups.addAll(Arrays.asList(group1, group2));
		when(userRepository.findOne(USER_ID)).thenReturn(userEntity);
		when(groupRepository.findByCreatedBy(userEntity)).thenReturn(Arrays.asList(groupEntity1, groupEntity2));
		when(conversionService.convert(groupRepository.findByCreatedBy(userEntity),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(GroupEntity.class)),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Group.class))))
						.thenReturn(Arrays.asList(group1, group2));
		// When
		List<Group> groups = systemUnderTest.listGroupsBelongingToUser(USER_ID);
		// Then
		assertEquals(expectedGroups, groups);
	}

	@Test(expected = UserDoesNotExistsException.class)
	public void shouldThrowUserDoesNotExistException() {
		systemUnderTest.listGroupsBelongingToUser(USER_ID);
	}

	@Test
	public void shouldReturnEmptyListWhenUserDoesNotHaveCreatedGroups() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID).build();
		when(userRepository.findOne(USER_ID)).thenReturn(userEntity);
		when(groupRepository.findByCreatedBy(userEntity)).thenReturn(new ArrayList<GroupEntity>());
		when(conversionService.convert(groupRepository.findByCreatedBy(userEntity),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(GroupEntity.class)),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Group.class))))
						.thenReturn(new ArrayList<Group>());
		// When
		List<Group> groups = systemUnderTest.listGroupsBelongingToUser(USER_ID);
		// Then
		assertTrue(groups.isEmpty());
	}
}
