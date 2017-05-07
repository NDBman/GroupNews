package com.epam.internship.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.internship.GroupService;
import com.epam.internship.dto.Group;

@RestController
@RequestMapping("/")
public class GroupController {

	@Autowired
	private GroupService groupService;
	
	@PostMapping("users/{userId}/groups")
	public Group createGroup(@PathVariable("userId") Long userId, @RequestParam String title,
			@RequestParam String description) {
		return groupService.createGoup(userId, title, description);
	}
	
	@GetMapping("users/{userId}/groups")
	public List<Group> getAllGroupsBelongingToUser(@PathVariable("userId")Long userId){
		return groupService.listGroupsBelongingToUser(userId);
	}
}
