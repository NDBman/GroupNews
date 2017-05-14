package com.epam.internship;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.internship.controller.MembershipController;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.Member;
import com.epam.internship.dto.Membership;
import com.epam.internship.dto.User;
import com.epam.internship.entity.Role;
import com.epam.internship.exception.GroupDoesNotExistsException;
import com.epam.internship.exception.LastAdminCannotBeRemovedException;
import com.epam.internship.exception.UserDoesNotExistsException;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { MembershipController.class })
public class MembershipControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MembershipService membershipService;

	private static final Gson GSON = new Gson();

	private static final Long GROUP_ID = 1L;
	private static final Long USER_ID = 1L;
	private static final Long MEMBERSHIP_ID = 1L;

	private static Member member;
	private static User user;
	private static Group group;
	private static Membership membership;

	private static List<Member> members;
	private static String membersJson;

	@BeforeClass
	public static void setUp() {
		// Given
		member = Member.builder().userId(USER_ID).role(Role.USER).build();
		members = Arrays.asList(member);
		membersJson = GSON.toJson(members);
		user = User.builder().id(USER_ID).build();
		group = Group.builder().id(GROUP_ID).build();
		membership = Membership.builder().id(MEMBERSHIP_ID).member(user).group(group).role(Role.USER).build();
	}

	@Test
	public void shouldReturnMembershipList() throws Exception {
		// Given
		when(membershipService.addUsersToGroup(GROUP_ID, members)).thenReturn(Arrays.asList(membership));
		// When
		mockMvc.perform(put("/groups/1/users").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(membersJson))
				// Then
				.andExpect(jsonPath("$[0].id").value(membership.getId().intValue()))
				.andExpect(jsonPath("$[0].member").value(user)).andExpect(jsonPath("$[0].group").value(group))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnNotFoundForNotExistingUserId() throws Exception {
		// Given
		when(membershipService.addUsersToGroup(GROUP_ID, members)).thenThrow(new UserDoesNotExistsException());
		// When
		mockMvc.perform(put("/groups/1/users").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(membersJson))
				// Then
				.andExpect(status().isNotFound());
	}

	@Test
	public void shouldReturnNotFoundForNotExistingGroupId() throws Exception {
		// Given
		when(membershipService.addUsersToGroup(10L, members)).thenThrow(new GroupDoesNotExistsException());
		// When
		mockMvc.perform(put("/groups/10/users").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(membersJson))
				// Then
				.andExpect(status().isNotFound());
	}

	@Test
	public void shouldReturnMethodNotAllowedForDeletingLastAdmin() throws Exception {
		// Given
		when(membershipService.addUsersToGroup(GROUP_ID, members)).thenThrow(new LastAdminCannotBeRemovedException());
		// When
		mockMvc.perform(put("/groups/1/users").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(membersJson))
				// Then
				.andExpect(status().isMethodNotAllowed());
	}
}
