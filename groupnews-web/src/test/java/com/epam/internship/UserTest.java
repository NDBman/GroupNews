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

import com.epam.internship.controller.UserController;
import com.epam.internship.dto.User;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserController.class})
public class UserTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	private User user1;
	private User user2;
	
	@Before
	public void setUp(){
		user1 = User.builder().name("Mr Brown").email("brown@test.com").build();
		user2 = User.builder().name("Mr green").email("green@test.com").build();
	}

	@Test
	public void shouldReturnUsers() throws Exception {
		Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
		System.out.println(user1.toString());
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("[" + user1.toString() + "," + user2.toString() + "]"));
	}
	
}
