package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Invalid form structure.")
public class InvalidFormException extends Exception {
	private static final long serialVersionUID = 2604760638951848101L;

	public InvalidFormException(String message) {
		super(message);
	}

	public InvalidFormException(String message, Throwable e) {
		super(message, e);
	}

}
