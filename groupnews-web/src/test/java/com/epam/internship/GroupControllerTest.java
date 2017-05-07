package com.epam.internship;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.internship.controller.GroupController;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.User;
import com.epam.internship.exception.UserDoesNotExistsException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { GroupController.class })
public class GroupControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GroupService groupService;

	private User user1;

	private final String GROUP_TITLE = "Group title";
	private final String GROUP_DESCRIPION = "Group Desc";

	private final Long USER_ID_1 = 1L;
	private final String USER_NAME_1 = "Mr Brown";
	private final String USER_EMAIL_1 = "brown@test.com";

	private final Long GROUP_ID_2 = 2L;
	private final String GROUP_TITLE_2 = "Group title 2";
	private final String GROUP_DESCRIPION_2 = "Group Desc 2";

	@Test
	public void shoudlReturnGroupWithGivenFields() throws Exception {
		// Given
		user1 = User.builder().id(USER_ID_1).name(USER_NAME_1).email(USER_EMAIL_1).build();
		Group group = Group.builder().id(1L).createdBy(user1).title(GROUP_TITLE).description(GROUP_DESCRIPION).build();
		when(groupService.createGoup(USER_ID_1, GROUP_TITLE, GROUP_DESCRIPION)).thenReturn(group);
		// When
		mockMvc.perform(post("/users/1/groups").param("title", GROUP_TITLE).param("description", GROUP_DESCRIPION))
				// Then
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.title").value(GROUP_TITLE))
				.andExpect(jsonPath("$.description").value(GROUP_DESCRIPION))
				.andExpect(jsonPath("$.createdBy.id").value(USER_ID_1))
				.andExpect(jsonPath("$.createdBy.name").value(USER_NAME_1))
				.andExpect(jsonPath("$.createdBy.email").value(USER_EMAIL_1)).andExpect(status().isOk());

	}

	@Test
	public void shouldReturnBadRequestWhenUserDoesNotExist() throws Exception {
		// Given
		when(groupService.createGoup(10L, GROUP_TITLE, GROUP_DESCRIPION)).thenThrow(new UserDoesNotExistsException());
		// When
		mockMvc.perform(post("/users/10/groups").param("title", GROUP_TITLE).param("description", GROUP_DESCRIPION))
				// Then
				.andExpect(status().isNotFound());
	}

	@Test
	public void shouldReturnBadRequestWhenGroupTitleIsBlank() throws Exception {
		when(groupService.createGoup(USER_ID_1, "", GROUP_DESCRIPION)).thenThrow(new IllegalArgumentException());
		mockMvc.perform(post("/users/1/groups").param("title", "").param("description", GROUP_DESCRIPION))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenGroupTitleIsLongerThanAllowed() throws Exception {
		// Given
		String longTitle = RandomStringUtils.random(71);
		when(groupService.createGoup(USER_ID_1, longTitle, GROUP_DESCRIPION)).thenThrow(new IllegalArgumentException());
		// When
		mockMvc.perform(post("/users/1/groups").param("title", longTitle).param("description", GROUP_DESCRIPION))
				// Then
				.andExpect(status().isBadRequest());
	}

	public void shouldReturnBadRequestWhenGroupDescIsLongerThanAllowed() throws Exception {
		// Given
		String longDesc = RandomStringUtils.random(2001);
		when(groupService.createGoup(USER_ID_1, GROUP_TITLE, longDesc)).thenThrow(new IllegalArgumentException());
		// When
		mockMvc.perform(post("/users/1/groups").param("title", GROUP_TITLE).param("description", longDesc))
				// Then
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnListOfGroupsBelongingToUser() throws Exception {
		// Given
		Group group1 = Group.builder().id(1L).title(GROUP_TITLE).description(GROUP_DESCRIPION).createdBy(user1).build();
		Group group2 = Group.builder().id(GROUP_ID_2).title(GROUP_TITLE_2).description(GROUP_DESCRIPION_2)
				.createdBy(user1).build();
		when(groupService.listGroupsBelongingToUser(USER_ID_1)).thenReturn(Arrays.asList(group1, group2));
		// When
		mockMvc.perform(get("/users/1/groups"))
				// Then
				// .andExpect(jsonPath("$[:1].id").value(group1.getId()))
				// .andExpect(jsonPath("$[:1].createdBy.id").value(group1.getCreatedBy().getId()))
				.andExpect(jsonPath("$[:1].createdBy.name").value(group1.getCreatedBy().getName()))
				.andExpect(jsonPath("$[:1].createdBy.email").value(group1.getCreatedBy().getEmail()))
				.andExpect(jsonPath("$[:1].title").value(group1.getTitle()))
				.andExpect(jsonPath("$[:1].description").value(group1.getDescription()))
				// .andExpect(jsonPath("$[1:2].id").value(group2.getId()))
				// .andExpect(jsonPath("$[1:2].createdBy.id").value(group2.getCreatedBy().getId()))
				.andExpect(jsonPath("$[1:2].createdBy.name").value(group2.getCreatedBy().getName()))
				.andExpect(jsonPath("$[1:2].createdBy.email").value(group2.getCreatedBy().getEmail()))
				.andExpect(jsonPath("$[1:2].title").value(group2.getTitle()))
				.andExpect(jsonPath("$[1:2].description").value(group2.getDescription())).andExpect(status().isOk());
	}

	@Test
	public void shouldReturnNotFound() throws Exception {
		// Given
		when(groupService.listGroupsBelongingToUser(10L)).thenThrow(new UserDoesNotExistsException());
		// When
		mockMvc.perform(get("/users/10/groups"))
				// Then
				.andExpect(status().isNotFound());

	}
}
