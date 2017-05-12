package com.epam.internship.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.epam.internship.dto.Membership;
import com.epam.internship.entity.MembershipEntity;

@Service
public class MembershipConverter implements Converter<Membership, MembershipEntity> {

	private UserConverter userConverter = new UserConverter();
	private GroupConverter groupConverter = new GroupConverter();

	@Override
	public MembershipEntity convert(Membership source) {
		MembershipEntity membershipEntity = MembershipEntity.builder().id(source.getId())
				.member(userConverter.convert(source.getMember()))
				.groupEntity(groupConverter.convert(source.getGroup())).role(source.getRole()).build();
		return membershipEntity;
	}

}
