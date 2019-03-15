package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "No data found for this request.")
public class NoDataException extends Exception {
	private static final long serialVersionUID = -535619314803394167L;

	public NoDataException(String message) {
		super(message);
	}

	public NoDataException(String message, Throwable e) {
		super(message, e);
	}

}
