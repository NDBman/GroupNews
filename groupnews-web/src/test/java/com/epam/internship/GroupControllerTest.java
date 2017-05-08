package com.epam.internship;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
