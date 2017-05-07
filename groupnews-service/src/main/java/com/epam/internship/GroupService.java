package com.epam.internship;

import java.util.List;

import com.epam.internship.dto.Group;

public interface GroupService {

	Group createGoup(Long userId, String title, String description);
	
	List<Group> listGroupsBelongingToUser(Long userId);
}
