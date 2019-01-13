package com.biit.form.manager.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.biit.form.manager.entity.CompanyUser;

public class UserFactory {
	private final static int MIN_CSV_LINE_LENGTH = 10;

	public static List<CompanyUser> createUsersFromCSV(String csvText) throws InvalidCsvFile {
		List<CompanyUser> users = new ArrayList<>();
		Scanner scanner = new Scanner(csvText);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.length() < MIN_CSV_LINE_LENGTH || !line.contains(";")) {
				continue;
			}
			String[] userFields = line.split(";");
			if (userFields.length < UserCsvFields.length()) {
				try {
					throw new InvalidCsvFile("Line '" + line + "' is not well formed.");
				} finally {
					scanner.close();
				}
			}

			CompanyUser companyUser = new CompanyUser(userFields[UserCsvFields.USERNAME.getCsvIndex()].trim(),
					userFields[UserCsvFields.PASSWORD.getCsvIndex()].trim(), createUniqueEmail(userFields[UserCsvFields.USERNAME.getCsvIndex()]), "", "",
					userFields[UserCsvFields.COMPANY.getCsvIndex()].trim(), userFields[UserCsvFields.FOLDER.getCsvIndex()]);
			users.add(companyUser);
		}
		scanner.close();
		return users;
	}

	private static String createUniqueEmail(String userName) {
		return userName + "@test.com";
	}
}
