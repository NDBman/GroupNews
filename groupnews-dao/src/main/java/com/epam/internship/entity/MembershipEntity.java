package com.epam.internship.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "memberships", uniqueConstraints = @UniqueConstraint(columnNames = { "member_id", "group_id" }) )
public class MembershipEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(nullable = false)
	private UserEntity member;
	@ManyToOne
	@JoinColumn(nullable = false)
	private GroupEntity group;
	@Column(nullable = false)
	private Role role;
}
