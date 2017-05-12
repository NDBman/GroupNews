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

	@SuppressWarnings("unchecked")
	@Override
	public List<Membership> addUsersToGroup(Long groupId, List<Member> newMembers) {
		GroupEntity groupEntity = groupRepository.findOne(groupId);
		if (groupEntity == null) {
			throw new GroupDoesNotExistsException();
		}
		List<MembershipEntity> membershipEntities = new ArrayList<>();
		for (Member member : newMembers) {
			UserEntity userEntity = userRepository.findOne(member.getUserId());
			if (userEntity == null) {
				throw new UserDoesNotExistsException();
			}
			MembershipEntity alreadyExistingMembership = membershipRepository.findByMemberAndGroupEntity(userEntity,
					groupEntity);
			if (alreadyExistingMembership == null) {
				membershipEntities.add(MembershipEntity.builder().member(userEntity).groupEntity(groupEntity)
						.role(member.getRole()).build());
			} else {
				canChangeRole(alreadyExistingMembership, member, groupEntity);
				alreadyExistingMembership.setRole(member.getRole());
				membershipEntities.add(alreadyExistingMembership);
			}

		}
		membershipEntities = membershipRepository.save(membershipEntities);
		return (List<Membership>) conversionService.convert(membershipEntities,
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(MembershipEntity.class)),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Membership.class)));
	}

	private void canChangeRole(MembershipEntity alreadyExistingMembership, Member newMember, GroupEntity groupEntity) {
		if (alreadyExistingMembership.getRole() == Role.ADMIN && newMember.getRole() == Role.USER) {
			int adminCounter = 0;
			for (MembershipEntity membershipEntity : membershipRepository.findByGroupEntity(groupEntity)) {
				if (membershipEntity.getRole() == Role.ADMIN) {
					adminCounter++;
				}
			}
			if (adminCounter == 1) {
				throw new LastAdminCannotBeRemovedException();
			}
		}
	}

}
