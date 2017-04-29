package com.epam.internship.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.epam.internship.dto.Group;
import com.epam.internship.entity.GroupEntity;

@Service
public class GroupConverter implements Converter<Group, GroupEntity> {
	
	private UserConverter userConverter = new UserConverter();

	@Override
	public GroupEntity convert(Group source) {
		return GroupEntity.builder().id(source.getId())
				.createdBy(userConverter.convert(source.getCreatedBy())).title(source.getTitle())
				.description(source.getDescription()).build();
	}

}
