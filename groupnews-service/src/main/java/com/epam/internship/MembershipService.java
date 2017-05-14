package com.epam.internship;

import java.util.List;

import com.epam.internship.dto.Member;
import com.epam.internship.dto.Membership;

public interface MembershipService {

	List<Membership> addUsersToGroup(Long groupId, List<Member> newMembers);

	Membership deleteMembership(Long groupId, Long userId);
}
