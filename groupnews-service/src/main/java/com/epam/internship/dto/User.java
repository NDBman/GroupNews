package com.epam.internship.dto;

import com.google.gson.Gson;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {

	private String name;
	private String email;
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
