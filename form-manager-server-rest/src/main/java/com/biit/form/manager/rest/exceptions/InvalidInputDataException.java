package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Invalid input data.")
public class InvalidInputDataException extends Exception {
	private static final long serialVersionUID = 2604760638951848101L;

	public InvalidInputDataException(String message) {
		super(message);
	}

	public InvalidInputDataException(String message, Throwable e) {
		super(message, e);
	}

}
