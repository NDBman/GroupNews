package com.epam.internship;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.epam.internship.entity.UserEntity;
import com.epam.internship.repo.UserRepository;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@Transactional
public class UserRepositoryIT {

	@Autowired
	private UserRepository systemUnderTest;

	private final String USER_NAME_1 = "Mr. Green";
	private final String USER_NAME_2 = "Mr. Brown";
	private final String USER_NAME_3 = "Mr. Blue";
	private final String USER_EMAIL_1 = "green@test.com";
	private final String USER_EMAIL_2 = "brown@test.com";
	private final String USER_EMAIL_3 = "blue@test.com";

	@Test
	public void count() {
		assertEquals(2, systemUnderTest.count());
	}

	@Test
	public void deleteOneOtherOneShouldStay() {
		// Given
		UserEntity userEntity = UserEntity.builder().name(USER_NAME_1).email("unique@email.hu").build();
		systemUnderTest.save(userEntity);
		// When
		systemUnderTest.delete(userEntity.getId());
		// Then
		assertEquals(2, systemUnderTest.count());
	}

	@Test
	public void findOneShouldReturnUserEntityWithId() {
		// When
		UserEntity userEntity = systemUnderTest.findOne(1L);
		// Then
		assertEquals(USER_NAME_1, userEntity.getName());
		assertEquals(USER_EMAIL_1, userEntity.getEmail());
	}

	@Test
	public void saveUserEntity() {
		// When
		UserEntity userEntity = systemUnderTest
				.save(UserEntity.builder().name(USER_NAME_3).email(USER_EMAIL_3).build());
		// Then
		assertEquals(3, systemUnderTest.count());
		assertEquals(USER_NAME_3, userEntity.getName());
		assertEquals(USER_EMAIL_3, userEntity.getEmail());
	}

	@Test
	public void findAllShouldReturnAllUserEntites() {
		// When
		List<UserEntity> userEntities = systemUnderTest.findAll();
		// Then
		assertEquals(2, userEntities.size());
		assertEquals(USER_NAME_1, userEntities.get(0).getName());
		assertEquals(USER_NAME_2, userEntities.get(1).getName());
		assertEquals(USER_EMAIL_1, userEntities.get(0).getEmail());
		assertEquals(USER_EMAIL_2, userEntities.get(1).getEmail());
	}

	// Cannot delete all user entities because some other entities may reference
	// them.
	@Test(expected = DataIntegrityViolationException.class)
	public void deleteAllShouldLeaveNoUserEntites() {
		// When
		systemUnderTest.deleteAll();
		// Then
		assertEquals(0, systemUnderTest.count());
	}

	@Test(expected = Exception.class)
	public void saveShouldFailForNullName() {
		UserEntity userEntity = UserEntity.builder().name(null).email("test@email.com").build();
		systemUnderTest.save(userEntity);
	}

	@Test(expected = Exception.class)
	public void saveShouldFailForAlreadyExistingEmail() {
		UserEntity userEntity = UserEntity.builder().name(USER_NAME_1).email(USER_EMAIL_1).build();
		systemUnderTest.save(userEntity);
	}
}
