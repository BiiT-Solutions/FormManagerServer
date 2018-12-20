package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown error.")
public class InternalServerException extends Exception {
	private static final long serialVersionUID = 2604760638951848101L;

	public InternalServerException(String message) {
		super(message);
	}

	public InternalServerException(String message, Throwable e) {
		super(message, e);
	}

	public InternalServerException(Throwable e) {
		super(e);
	}

}
