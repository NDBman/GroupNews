package com.epam.internship.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.epam.internship.GroupService;
import com.epam.internship.UserService;
import com.epam.internship.dto.Group;
import com.epam.internship.dto.User;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.repo.GroupRepository;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ConversionService conversionService;

	@Override
	public Group ceateGoup(Long userId, String title, String description) {
		Group group = Group.builder().title(title).description(description)
				.createdBy(conversionService.convert(userService.getUserById(userId), User.class)).build();
		groupRepository.save(conversionService.convert(group, GroupEntity.class));
		return group;
	}

}
