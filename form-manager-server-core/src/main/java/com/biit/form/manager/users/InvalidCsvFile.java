package com.biit.form.manager.users;

public class InvalidCsvFile extends Exception {
	private static final long serialVersionUID = -7030841984649479574L;

	public InvalidCsvFile(String message) {
		super(message);
	}

	public InvalidCsvFile(String message, Throwable e) {
		super(message, e);
	}

	public InvalidCsvFile(Throwable e) {
		super(e);
	}

}
