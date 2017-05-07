package com.epam.internship;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.internship.controller.UserController;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.User;
import com.epam.internship.exception.UserDoesNotExistsException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { UserController.class })
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private GroupService groupService;

	private User user1;
	private User user2;
	private User user3;

	private final String CREATE_NAME = "Joe";
	private final String CREATE_EMAIL = "test@alma.com";

	private final Long USER_ID_1 = 1L;
	private final String USER_NAME_1 = "Mr Brown";
	private final String USER_EMAIL_1 = "brown@test.com";

	private final String USER_NAME_2 = "Mr green";
	private final String USER_EMAIL_2 = "green@test.com";

	private final String GROUP_TITLE = "Group title";
	private final String GROUP_DESCRIPION = "Group Desc";

	@Before
	public void setUp() {
		// Given
		user1 = User.builder().id(USER_ID_1).name(USER_NAME_1).email(USER_EMAIL_1).build();
		user2 = User.builder().name(USER_NAME_2).email(USER_EMAIL_2).build();

		user3 = User.builder().name(CREATE_NAME).email(CREATE_EMAIL).build();
	}

	@Test
	public void shouldReturnUsers() throws Exception {
		// Given
		Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
		// When
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andDo(MockMvcResultHandlers.print())
				// Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[:1].name").value(user1.getName()))
				.andExpect(jsonPath("$[:1].email").value(user1.getEmail()))
				.andExpect(jsonPath("$[1:2].name").value(user2.getName()))
				.andExpect(jsonPath("$[1:2].email").value(user2.getEmail()));

	}

	@Test
	public void shouldCreateUser() throws Exception {
		// Given
		Mockito.when(userService.createUser(CREATE_NAME, CREATE_EMAIL)).thenReturn(user3);
		// When
		mockMvc.perform(
				MockMvcRequestBuilders.post("/users/new").param("name", CREATE_NAME).param("email", CREATE_EMAIL))
				.andDo(MockMvcResultHandlers.print())
				// Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CREATE_NAME))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(CREATE_EMAIL));
	}

	@Test
	public void shouldReturnBadRequestStatus() throws Exception {
		// Given
		Mockito.when(userService.createUser("", "")).thenThrow(new IllegalArgumentException());
		// When
		mockMvc.perform(MockMvcRequestBuilders.post("/users/new").param("name", "").param("email", ""))
				// Then
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void shouldReturnConflictStatus() throws Exception {
		// Given
		Mockito.when(userService.createUser("", ""))
				.thenThrow(new DataIntegrityViolationException("This email is already used."));
		// When
		mockMvc.perform(MockMvcRequestBuilders.post("/users/new").param("name", "").param("email", ""))
				// Then
				.andExpect(MockMvcResultMatchers.status().isConflict());
	}

	@Test
	public void shouldReturnSingleUser() throws Exception {
		// Given
		Mockito.when(userService.getUserById(USER_ID_1)).thenReturn(user1);
		// When
		mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(MockMvcResultHandlers.print())
				// Then
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(USER_NAME_1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(USER_EMAIL_1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void shouldReturnBadRequestStatusForInvalidPathVariable() throws Exception {
		// When
		mockMvc.perform(MockMvcRequestBuilders.get("/users/pistike")).andDo(MockMvcResultHandlers.print())
				// Then
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void shoudlReturnGroupWithGivenFields() throws Exception {
		// Given
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
