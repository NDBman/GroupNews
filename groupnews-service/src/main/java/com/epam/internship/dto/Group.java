package com.epam.internship.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Group {

	private Long id;
	private User createdBy;
	private String title;
	private String description;
}
