package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Invalid form structure.")
public class PdfNotGeneratedException extends Exception {
	private static final long serialVersionUID = 6575029922071452858L;

	public PdfNotGeneratedException(String message) {
		super(message);
	}

	public PdfNotGeneratedException(String message, Throwable e) {
		super(message, e);
	}

}
