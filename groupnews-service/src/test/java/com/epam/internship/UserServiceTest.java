package com.epam.internship;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.test.context.junit4.SpringRunner;

import com.epam.internship.dto.User;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.impl.UserServiceImpl;
import com.epam.internship.repo.UserRepository;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	private final String USER_NAME_1 = "Mr. Brown";
	private final String USER_NAME_2 = "Mr. Green";
	private final String USER_EMAIL_1 = "brown@test.com";
	private final String USER_EMAIL_2 = "green@test.com";

	@Mock
	private UserRepository userRepository;

	@Mock
	private ConversionService conversionService;

	private UserEntity userEntity1;
	private UserEntity userEntity2;

	private User user1;
	private User user2;

	@InjectMocks
	private UserServiceImpl systemUnderTest;

	@Before
	public void setUp() {
		userEntity1 = UserEntity.builder().name(USER_NAME_1).email(USER_EMAIL_1).build();
		userEntity2 = UserEntity.builder().name(USER_NAME_2).email(USER_EMAIL_2).build();
		user1 = User.builder().name(USER_NAME_1).email(USER_EMAIL_1).build();
		user2 = User.builder().name(USER_NAME_2).email(USER_EMAIL_2).build();
		Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity1, userEntity2));
		Mockito.when(userRepository.save(userEntity1)).thenReturn(userEntity1);
		Mockito.when(conversionService.convert(userRepository.findAll(),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserEntity.class)),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(User.class))))
				.thenReturn(Arrays.asList(user1, user2));
	}

	@Test
	public void shoudlReturnAllUsers() {
		System.out.println(systemUnderTest.getAllUsers());
		List<User> users = systemUnderTest.getAllUsers();
		assertEquals(users.get(0).getName(), USER_NAME_1);
		assertEquals(users.get(0).getEmail(), USER_EMAIL_1);
		assertEquals(users.get(1).getName(), USER_NAME_2);
		assertEquals(users.get(1).getEmail(), USER_EMAIL_2);
	}

	@Test
	public void shouldCreateUser() {
		User user = systemUnderTest.createUser(USER_NAME_1, USER_EMAIL_1);
		assertEquals(USER_NAME_1, user.getName());
		assertEquals(USER_EMAIL_1, user.getEmail());
	}
}
