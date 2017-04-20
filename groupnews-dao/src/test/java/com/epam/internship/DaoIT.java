package com.epam.internship;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.epam.internship.entity.UserEntity;
import com.epam.internship.repo.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
@Rollback
public class DaoIT {

	@Autowired
	private UserRepository systemUnderTest;

	private final String USER_NAME_1 = "Mr. Green";
	private final String USER_NAME_2 = "Mr. Brown";
	private final String USER_NAME_3 = "Mr. Blue";
	private final String USER_EMAIL_1 = "green@test.com";
	private final String USER_EMAIL_2 = "brown@test.com";
	private final String USER_EMAIL_3 = "blue@test.com";

	@Test
	@Transactional
	public void count() {
		assertEquals(2, systemUnderTest.count());
	}

	@Test
	@Transactional
	public void deleteOneOtherTwoShouldStay() {
		//When
		systemUnderTest.delete(1L);
		//Then
		assertEquals(1, systemUnderTest.count());
		List<UserEntity> userEntites = systemUnderTest.findAll();
		assertEquals(2L, userEntites.get(0).getId().longValue());
	}

	@Test
	@Transactional
	public void findOneShouldReturnUserEntityWithIdTest() {
		//When
		UserEntity userEntity = systemUnderTest.findOne(1L);
		//Then
		assertEquals(USER_NAME_1, userEntity.getName());
		assertEquals(USER_EMAIL_1, userEntity.getEmail());
	}

	@Test
	@Transactional
	public void saveUserEntityTest() {
		//When
		UserEntity userEntity = systemUnderTest
				.save(UserEntity.builder().name(USER_NAME_3).email(USER_EMAIL_3).build());
		//Then
		assertEquals(3, systemUnderTest.count());
		assertEquals(USER_NAME_3, userEntity.getName());
		assertEquals(USER_EMAIL_3, userEntity.getEmail());
	}
	
	@Test
	@Transactional
	public void findAllShouldReturnAllUserEntitesTest(){
		//When
		List<UserEntity> userEntities = systemUnderTest.findAll();
		//Then
		assertEquals(2, userEntities.size());
		assertEquals(USER_NAME_1, userEntities.get(0).getName());
		assertEquals(USER_NAME_2, userEntities.get(1).getName());
		assertEquals(USER_EMAIL_1, userEntities.get(0).getEmail());
		assertEquals(USER_EMAIL_2, userEntities.get(1).getEmail());
	}
	
	@Test
	@Transactional
	public void deleteAllShouldLeaveNoUserEntites(){
		//When
		systemUnderTest.deleteAll();
		//Then
		assertEquals(0, systemUnderTest.count());
	}
}
