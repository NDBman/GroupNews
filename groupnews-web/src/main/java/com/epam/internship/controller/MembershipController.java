package com.epam.internship.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.internship.MembershipService;
import com.epam.internship.dto.Member;
import com.epam.internship.dto.Membership;

@RestController
@RequestMapping("/")
public class MembershipController {

	@Autowired
	private MembershipService membershipService;

	@PutMapping("groups/{groupId}/users")
	public List<Membership> updateGroupMembers(@PathVariable("groupId") Long groupId,
			@RequestBody List<Member> newMembers) {
		return membershipService.addUsersToGroup(groupId, newMembers);
	}
}
