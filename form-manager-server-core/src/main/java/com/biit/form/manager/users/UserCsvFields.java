package com.biit.form.manager.users;

public enum UserCsvFields {
	COMPANY(0), FOLDER(1), USERNAME(2), PASSWORD(3);

	private final int csvIndex;

	private UserCsvFields(int csvIndex) {
		this.csvIndex = csvIndex;
	}

	public int getCsvIndex() {
		return csvIndex;
	}

	public static int length() {
		return UserCsvFields.values().length;
	}
}
