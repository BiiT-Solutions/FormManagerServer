package com.biit.form.manager.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Error when uploading the file.")
public class FileNotUploadedException extends Exception {
	private static final long serialVersionUID = 2604760638951848101L;

	public FileNotUploadedException(String message) {
		super(message);
	}

	public FileNotUploadedException(String message, Throwable e) {
		super(message, e);
	}

}
