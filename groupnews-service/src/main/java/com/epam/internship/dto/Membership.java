package com.epam.internship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

	private Long id;
	private User member;
	private Group group;
	private String role;
}
