package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "User not stored into database.")
public class UserDoesNotExistsException extends Exception {
	private static final long serialVersionUID = 9106350377910563925L;

	public UserDoesNotExistsException(String message) {
		super(message);
	}

	public UserDoesNotExistsException(String message, Throwable e) {
		super(message, e);
	}

}
