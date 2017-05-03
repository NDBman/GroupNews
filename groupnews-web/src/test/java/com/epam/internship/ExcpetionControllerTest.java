package com.epam.internship;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.internship.controller.UserController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
public class ExcpetionControllerTest {

	@Autowired
	private MockMvc mockMVc;

	@MockBean
	private UserService userService;

	@Test
	public void shouldReturnBadRequestStatus() throws Exception {
		// Given
		Mockito.when(userService.createUser("", "")).thenThrow(new IllegalArgumentException());
		// When
		mockMVc.perform(MockMvcRequestBuilders.post("/users/new").param("name", "").param("email", ""))
				// Then
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void shouldReturnConflictStatus() throws Exception {
		// Given
		Mockito.when(userService.createUser("", ""))
				.thenThrow(new DataIntegrityViolationException("This email is already used."));
		// When
		mockMVc.perform(MockMvcRequestBuilders.post("/users/new").param("name", "").param("email", ""))
				// Then
				.andExpect(MockMvcResultMatchers.status().isConflict());
	}
}
