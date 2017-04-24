package com.epam.internship;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.internship.controller.UserController;
import com.epam.internship.dto.User;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { UserController.class })
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

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
	
	@Before
	public void setUp() {
		// Given
		user1 = User.builder().name(USER_NAME_1).email(USER_EMAIL_1).build();
		user2 = User.builder().name(USER_NAME_2).email(USER_EMAIL_2).build();

		user3 = User.builder().name(CREATE_NAME).email(CREATE_EMAIL).build();
	}

	@Test
	public void shouldReturnUsers() throws Exception {
		// When
		Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
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
		mockMvc.perform(MockMvcRequestBuilders.post("/users/new").param("name", CREATE_NAME).param("email", CREATE_EMAIL))
				.andDo(MockMvcResultHandlers.print())
				// Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CREATE_NAME))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(CREATE_EMAIL));
	}

	@Test
	public void shouldReturnBadRequestStatus() throws Exception {
		// When
		mockMvc.perform(MockMvcRequestBuilders.post("/new").param("name", "").param("email", CREATE_EMAIL))
				.andDo(MockMvcResultHandlers.print())
				// Then
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}


	
	@Test
	public void shoudlReturnNotFoundStatus() throws Exception {
		//When
		mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(MockMvcResultHandlers.print())
			//Then
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void shouldReturnBadRequestStatusForInvalidPathVariable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users/pistike")).andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
