package com.epam.internship.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.epam.internship.dto.Group;
import com.epam.internship.entity.GroupEntity;

@Service
public class GroupEntityConverter implements Converter<GroupEntity, Group> {

	@Autowired
	private UserEntityConverter UserEntityConverter;

	@Override
	public Group convert(GroupEntity source) {
		return Group.builder().id(source.getId()).createdBy(UserEntityConverter.convert(source.getCreatedBy()))
				.title(source.getTitle()).description(source.getDescription()).build();
	}

}
