package com.epam.internship.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import com.epam.internship.MembershipService;
import com.epam.internship.dto.Member;
import com.epam.internship.dto.Membership;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.Role;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.exception.GroupDoesNotExistsException;
import com.epam.internship.exception.LastAdminCannotBeRemovedException;
import com.epam.internship.exception.UserDoesNotExistsException;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.MembershipRepository;
import com.epam.internship.repo.UserRepository;

@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private MembershipRepository membershipRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GroupRepository groupRepository;

	private GroupEntity getGroup(Long groupId) {
		GroupEntity groupEntity = groupRepository.findOne(groupId);
		if (groupEntity == null) {
			throw new GroupDoesNotExistsException();
		}
		return groupEntity;
	}

	private UserEntity getUser(Long userId) {
		UserEntity userEntity = userRepository.findOne(userId);
		if (userEntity == null) {
			throw new UserDoesNotExistsException();
		}
		return userEntity;
	}

	private void changeRoleInListElement(List<MembershipEntity> membershipEntities, MembershipEntity membershipEntity,
			Member member) {
		int membershipIndex = membershipEntities.indexOf(membershipEntity);
		membershipEntities.get(membershipIndex).setRole(Role.valueOf(member.getRole()));
	}

	private void createMembeshipIfItDidNotExistedBefore(List<MembershipEntity> membershipEntities,
			UserEntity userEntity, GroupEntity groupEntity, Member member) {
		boolean updateUserOnlyOnce = true;
		for (MembershipEntity me : membershipEntities) {
			if (me.getMember().equals(userEntity)) {
				changeRoleInListElement(membershipEntities, me, member);
				updateUserOnlyOnce = false;
				break;
			}
		}
		if (updateUserOnlyOnce) {
			membershipEntities.add(MembershipEntity.builder().member(userEntity).group(groupEntity)
					.role(Role.valueOf(member.getRole())).build());
		}
	}

	private void processCreateMembershipRequests(List<MembershipEntity> membershipEntities, List<Member> newMembers,
			GroupEntity groupEntity) {
		for (Member member : newMembers) {
			UserEntity userEntity = getUser(member.getUserId());
			MembershipEntity alreadyExistingMembership = membershipRepository.findByMemberAndGroup(userEntity,
					groupEntity);
			if (alreadyExistingMembership == null) {
				createMembeshipIfItDidNotExistedBefore(membershipEntities, userEntity, groupEntity, member);
			} else {
				if (alreadyExistingMembership.getRole() == Role.ADMIN && member.getRole().equals("USER")) {
					checkIfCanChangeRole(groupEntity);
				}
				if (membershipEntities.contains(alreadyExistingMembership)) {
					changeRoleInListElement(membershipEntities, alreadyExistingMembership, member);
				} else {
					alreadyExistingMembership.setRole(Role.valueOf(member.getRole()));
					membershipEntities.add(alreadyExistingMembership);
				}

			}

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Membership> addUsersToGroup(Long groupId, List<Member> newMembers) {
		GroupEntity groupEntity = getGroup(groupId);
		List<MembershipEntity> membershipEntities = new ArrayList<>();
		processCreateMembershipRequests(membershipEntities, newMembers, groupEntity);
		membershipEntities = membershipRepository.save(membershipEntities);
		return (List<Membership>) conversionService.convert(membershipEntities,
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(MembershipEntity.class)),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Membership.class)));
	}

	private void checkIfCanChangeRole(GroupEntity groupEntity) {

		long adminCounter = membershipRepository.findByGroup(groupEntity).stream()
				.filter(m -> m.getRole().equals(Role.ADMIN)).count();
		if (adminCounter == 1) {
			throw new LastAdminCannotBeRemovedException();
		}
	}

	@Override
	public Membership deleteMembership(Long groupId, Long userId) {
		UserEntity userEntity = userRepository.findOne(userId);
		if (userEntity == null) {
			throw new UserDoesNotExistsException();
		}
		GroupEntity groupEntity = groupRepository.findOne(groupId);
		if (groupEntity == null) {
			throw new GroupDoesNotExistsException();
		}
		MembershipEntity membershipEntity = membershipRepository.findByMemberAndGroup(userEntity, groupEntity);
		if (membershipEntity.getRole() == Role.ADMIN) {
			checkIfCanChangeRole(groupEntity);
		}
		membershipRepository.delete(membershipEntity);

		return conversionService.convert(membershipEntity, Membership.class);
	}

}
