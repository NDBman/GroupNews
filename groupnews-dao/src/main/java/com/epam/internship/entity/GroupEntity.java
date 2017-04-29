package com.epam.internship.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name="groups")
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	@OneToOne(targetEntity=UserEntity.class)
	private UserEntity createdBy;
	@Column(nullable = false, length = 70)
	private String title;
	@Column(length = 2000)
	private String description;
}
