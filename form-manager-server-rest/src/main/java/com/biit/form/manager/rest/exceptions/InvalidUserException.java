package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid user credentials.")
public class InvalidUserException extends Exception {
	private static final long serialVersionUID = 2604760638951848101L;

	public InvalidUserException(String message) {
		super(message);
	}

	public InvalidUserException(String message, Throwable e) {
		super(message, e);
	}

}
