package com.epam.internship.impl;

import javax.transaction.Transactional;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.epam.internship.GroupService;
import com.epam.internship.dto.Group;
import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.UserEntity;
import com.epam.internship.exception.UserDoesNotExistsException;
import com.epam.internship.repo.GroupRepository;
import com.epam.internship.repo.UserRepository;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConversionService conversionService;

	@Override
	public Group createGoup(Long userId, String title, String description) {
		UserEntity userEntity = userRepository.findOne(userId);
		if (userEntity == null) {
			throw new UserDoesNotExistsException();
		}

		Validate.notBlank(title);
		Validate.inclusiveBetween(1, 70, title.length());
		Validate.inclusiveBetween(1, 2000, description.length());

		GroupEntity groupEntity = GroupEntity.builder().title(title).description(description).createdBy(userEntity)
				.build();
		groupEntity = groupRepository.save(groupEntity);
		return conversionService.convert(groupEntity, Group.class);
	}

}
