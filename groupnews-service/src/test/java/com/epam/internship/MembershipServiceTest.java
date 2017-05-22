package com.epam.internship;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import com.epam.internship.dto.Member;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.Role;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.exception.GroupDoesNotExistsException;
import com.epam.internship.exception.LastAdminCannotBeRemovedException;
import com.epam.internship.exception.UserDoesNotExistsException;
import com.epam.internship.impl.MembershipServiceImpl;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.MembershipRepository;
import com.epam.internship.repo.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class MembershipServiceTest {

	@Mock
	private GroupRepository groupRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private MembershipRepository membershipRepository;

	@Mock
	private ConversionService conversionService;

	@InjectMocks
	private MembershipServiceImpl systemUnderTest;

	@Captor
	private ArgumentCaptor<List<MembershipEntity>> membershipsCaptor;

	@Captor
	private ArgumentCaptor<MembershipEntity> membershipCaptor;

	private final Long USER_ID_1 = 1L;
	private final String USER_NAME_1 = "Mr. Brown";
	private final String USER_EMAIL_1 = "brown@email.com";

	private final Long USER_ID_2 = 2L;
	private final String USER_NAME_2 = "Mr. Green";
	private final String USER_EMAIL_2 = "green@email.com";

	private final Long GROUP_ID = 1L;
	private final String GROUP_TITLE = "Group Title";
	private final String GROUP_DESCRIPTION = "Group Description";

	private final Long MEMBERSHIP_ID = 1L;

	@Test
	public void shouldAddUsersToGroup() {
		// Given
		UserEntity userEntity1 = UserEntity.builder().id(USER_ID_1).name(USER_NAME_1).email(USER_EMAIL_1).build();
		UserEntity userEntity2 = UserEntity.builder().id(USER_ID_2).name(USER_NAME_2).email(USER_EMAIL_2).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).title(GROUP_TITLE).description(GROUP_DESCRIPTION)
				.createdBy(userEntity1).build();
		Member member1 = Member.builder().userId(USER_ID_1).role(Role.USER).build();
		Member member2 = Member.builder().userId(USER_ID_2).role(Role.ADMIN).build();
		MembershipEntity membershipEntity1 = MembershipEntity.builder().member(userEntity1).group(groupEntity)
				.role(member1.getRole()).build();
		MembershipEntity membershipEntity2 = MembershipEntity.builder().member(userEntity2).group(groupEntity)
				.role(member2.getRole()).build();
		List<MembershipEntity> membershipEntities = Arrays.asList(membershipEntity1, membershipEntity2);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(groupEntity);
		when(userRepository.findOne(USER_ID_1)).thenReturn(userEntity1);
		when(userRepository.findOne(USER_ID_2)).thenReturn(userEntity2);

		// When
		systemUnderTest.addUsersToGroup(GROUP_ID, Arrays.asList(member1, member2));

		// Then
		verify(membershipRepository).save(membershipsCaptor.capture());
		List<MembershipEntity> actualMembershipEntities = membershipsCaptor.getValue();
		assertEquals(membershipEntities, actualMembershipEntities);
	}

	@Test(expected = GroupDoesNotExistsException.class)
	public void shouldThrowGroupDoesNotExistException() {
		// Given
		when(groupRepository.findOne(GROUP_ID)).thenReturn(null);
		// When
		systemUnderTest.addUsersToGroup(GROUP_ID, Arrays.asList());
	}

	@Test(expected = UserDoesNotExistsException.class)
	public void shouldThrowUserDoesNotExistException() {
		// Given
		when(groupRepository.findOne(GROUP_ID)).thenReturn(new GroupEntity());
		when(userRepository.findOne(USER_ID_1)).thenReturn(null);
		systemUnderTest.addUsersToGroup(GROUP_ID,
				Arrays.asList(Member.builder().userId(USER_ID_1).role(Role.USER).build()));
	}

	@Test
	public void shouldUpdateExistingUserRoleFromUserToAdmin() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID_1).name(USER_NAME_1).email(USER_EMAIL_1).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).createdBy(new UserEntity()).title(GROUP_TITLE)
				.description(GROUP_DESCRIPTION).build();
		MembershipEntity membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID).member(userEntity)
				.group(groupEntity).role(Role.USER).build();

		when(groupRepository.findOne(GROUP_ID)).thenReturn(groupEntity);
		when(userRepository.findOne(USER_ID_1)).thenReturn(userEntity);
		when(membershipRepository.findByMemberAndGroup(userEntity, groupEntity)).thenReturn(membershipEntity);

		/// When
		systemUnderTest.addUsersToGroup(GROUP_ID,
				Arrays.asList(Member.builder().userId(USER_ID_1).role(Role.ADMIN).build()));
		verify(membershipRepository).save(membershipsCaptor.capture());
		// Then
		assertEquals(Role.ADMIN, membershipsCaptor.getValue().get(0).getRole());
	}

	@Test(expected = LastAdminCannotBeRemovedException.class)
	public void shouldThrowLastAdminCannotBeRemovedException() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID_1).name(USER_NAME_1).email(USER_EMAIL_1).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).createdBy(new UserEntity()).title(GROUP_TITLE)
				.description(GROUP_DESCRIPTION).build();
		MembershipEntity membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID).member(userEntity)
				.group(groupEntity).role(Role.ADMIN).build();

		when(groupRepository.findOne(GROUP_ID)).thenReturn(groupEntity);
		when(userRepository.findOne(USER_ID_1)).thenReturn(userEntity);
		when(membershipRepository.findByMemberAndGroup(userEntity, groupEntity)).thenReturn(membershipEntity);
		when(membershipRepository.findByGroup(groupEntity)).thenReturn(Arrays.asList(membershipEntity));
		// When
		systemUnderTest.addUsersToGroup(GROUP_ID,
				Arrays.asList(Member.builder().userId(USER_ID_1).role(Role.USER).build()));
	}

	@Test
	public void shouldDeleteMembership() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID_1).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).build();
		MembershipEntity membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID).member(userEntity)
				.group(groupEntity).role(Role.USER).build();
		when(userRepository.findOne(USER_ID_1)).thenReturn(userEntity);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(groupEntity);
		when(membershipRepository.findByMemberAndGroup(userEntity, groupEntity)).thenReturn(membershipEntity);
		// When
		systemUnderTest.deleteMembership(GROUP_ID, USER_ID_1);
		// Then
		verify(membershipRepository).delete(membershipCaptor.capture());
		assertEquals(membershipEntity, membershipCaptor.getValue());
	}

	@Test(expected = UserDoesNotExistsException.class)
	public void shouldThrowUserDoesNotExistExceptionWhenDeleteIsCalled() {
		// Given
		when(userRepository.findOne(USER_ID_1)).thenReturn(null);
		// When
		systemUnderTest.deleteMembership(GROUP_ID, USER_ID_1);
	}

	@Test(expected = GroupDoesNotExistsException.class)
	public void shouldThrowGroupDoesNotExistExceptionWhenDeleteIsCalled() {
		// Given
		when(userRepository.findOne(USER_ID_1)).thenReturn(new UserEntity());
		when(groupRepository.findOne(GROUP_ID)).thenReturn(null);
		// When
		systemUnderTest.deleteMembership(GROUP_ID, USER_ID_1);
	}

	@Test(expected = LastAdminCannotBeRemovedException.class)
	public void shouldThrowLastAdminCannotBeRemovedExceptionWhenDeleteIsCalled() {
		// Given
		UserEntity userEntity = UserEntity.builder().id(USER_ID_1).build();
		GroupEntity groupEntity = GroupEntity.builder().id(GROUP_ID).build();
		MembershipEntity membershipEntity = MembershipEntity.builder().id(MEMBERSHIP_ID).member(userEntity)
				.group(groupEntity).role(Role.ADMIN).build();
		when(userRepository.findOne(USER_ID_1)).thenReturn(userEntity);
		when(groupRepository.findOne(GROUP_ID)).thenReturn(groupEntity);
		when(membershipRepository.findByMemberAndGroup(userEntity, groupEntity)).thenReturn(membershipEntity);
		when(membershipRepository.findByGroup(groupEntity)).thenReturn(Arrays.asList(membershipEntity));
		// When
		systemUnderTest.deleteMembership(GROUP_ID, USER_ID_1);
	}
}
