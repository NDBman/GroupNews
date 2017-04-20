package com.epam.internship;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.epam.internship.controller.UserController;
import com.epam.internship.dto.User;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService systemUnderTest;
	
	private User user1;
	private User user2;
	private User user3;
	
	private final String CREATE_NAME = "Joe";
	private final String CREATE_EMAIL = "test@alma.com";
	
	
	@Before
	public void setUp(){
		//Given
		user1 = User.builder().name("Mr Brown").email("brown@test.com").build();
		user2 = User.builder().name("Mr green").email("green@test.com").build();
		
		user3 = User.builder().name(CREATE_NAME).email(CREATE_EMAIL).build();
	}

	@Test
	public void shouldReturnUsers() throws Exception {
		//When
		Mockito.when(systemUnderTest.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andDo(MockMvcResultHandlers.print())
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[:1].name").value(user1.getName()))
				.andExpect(jsonPath("$[:1].email").value(user1.getEmail()))
				.andExpect(jsonPath("$[1:2].name").value(user2.getName()))
				.andExpect(jsonPath("$[1:2].email").value(user2.getEmail()));
				
	}
	
	@Test
	public void shouldCreateUser() throws Exception{
		//Given
		Mockito.when(systemUnderTest.createUser(CREATE_NAME, CREATE_EMAIL)).thenReturn(user3);
		//When
		mockMvc.perform(MockMvcRequestBuilders.post("/new").param("name", CREATE_NAME).param("email", CREATE_EMAIL))
				.andDo(MockMvcResultHandlers.print())
				//Then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CREATE_NAME))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(CREATE_EMAIL));
	}
	
	@Test
	public void shouldReturnBadRequestStatus() throws Exception{
		//When
		mockMvc.perform(MockMvcRequestBuilders.post("/new").param("name", "").param("email", CREATE_EMAIL))
		.andDo(MockMvcResultHandlers.print())
		//Then
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void shouldReturnConflictStatus() throws Exception{
		Mockito.when(systemUnderTest.emailAlreadyExists(CREATE_EMAIL)).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.post("/new").param("name", CREATE_NAME).param("email", CREATE_EMAIL))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isConflict());
	}
}
