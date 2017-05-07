package com.epam.internship.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.internship.entity.GroupEntity;
import com.epam.internship.entity.UserEntity;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

	List<GroupEntity> findByCreatedBy(UserEntity userEntity);
}
