package com.epam.internship;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.epam.internship.converter.RoleConverter;
import com.epam.internship.entity.Role;
import com.epam.internship.exception.RoleDoesNotExistsException;

public class RoleConverterTest {

	private RoleConverter systemUnderTest = new RoleConverter();
	
	private final String ROLE_USER = "USER";
	private final String ROLE_ADMIN = "ADMIN";
	
	@Test
	public void shouldReturnUserRole(){
		assertEquals(Role.USER, systemUnderTest.convert(ROLE_USER));
	}
	
	@Test
	public void shouldReturnAdminRole(){
		assertEquals(Role.ADMIN, systemUnderTest.convert(ROLE_ADMIN));
	}
	
	@Test(expected = RoleDoesNotExistsException.class)
	public void shouldThrowRoleDoesNotExistsException(){
		systemUnderTest.convert("wrong role value");
	}
}
