package com.epam.internship;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.internship.impl.MembershipServiceImpl;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.MembershipRepository;
import com.epam.internship.repo.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class MembershipServiceTest {

	@Mock
	private GroupRepository groupRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private MembershipRepository membershipRepository;
	
	@InjectMocks
	private MembershipServiceImpl membershipServiceImpl;
	
	
}
