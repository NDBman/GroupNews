package com.epam.internship.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.internship.exception.GroupDoesNotExistsException;
import com.epam.internship.exception.LastAdminCannotBeRemovedException;
import com.epam.internship.exception.UserDoesNotExistsException;

@ControllerAdvice
public class ExceptionController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({IllegalArgumentException.class})
	public void handleBadRequest() {

	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public void handleConflict() {

	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({UserDoesNotExistsException.class, GroupDoesNotExistsException.class})
	public void handleNotFound() {

	}
	
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(LastAdminCannotBeRemovedException.class)
	public void handleNotAllowed(){
		
	}
	
}
