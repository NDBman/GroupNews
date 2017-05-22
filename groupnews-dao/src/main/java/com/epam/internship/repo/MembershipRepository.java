package com.epam.internship.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.MembershipEntity;
import com.epam.internship.entity.UserEntity;

@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {

	List<MembershipEntity> findByMember(UserEntity userEntity);

	List<MembershipEntity> findByGroup(GroupEntity groupEntity);
	
	MembershipEntity findByMemberAndGroup(UserEntity userEntity, GroupEntity groupEntity);
}
