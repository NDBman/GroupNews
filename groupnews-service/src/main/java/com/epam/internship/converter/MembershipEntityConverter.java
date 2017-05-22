package com.epam.internship.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.epam.internship.dto.Membership;
import com.epam.internship.entity.MembershipEntity;

@Service
public class MembershipEntityConverter implements Converter<MembershipEntity, Membership> {

	@Autowired
	private UserEntityConverter userEntityConverter;
	@Autowired
	private GroupEntityConverter groupEntityConverter;

	@Override
	public Membership convert(MembershipEntity source) {
		Membership membership = Membership.builder().id(source.getId())
				.member(userEntityConverter.convert(source.getMember()))
				.group(groupEntityConverter.convert(source.getGroup())).role(source.getRole()).build();
		return membership;
	}

}
