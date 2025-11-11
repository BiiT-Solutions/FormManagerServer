package com.biit.form.manager.users;

/*-
 * #%L
 * Form Manager Server (Core)
 * %%
 * Copyright (C) 2019 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.entity.CompanyUser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class UserFactory {

	public static List<CompanyUser> createUsersFromCSV(String csvText) throws InvalidCsvFile {
		List<CompanyUser> users = new ArrayList<>();

		try (Reader inputString = new StringReader(csvText)) {
			try (CSVReader csvReader = new CSVReaderBuilder(inputString).withCSVParser(
					new CSVParserBuilder().withSeparator(FormManagerConfigurationReader.getInstance().getCsvSeparator().charAt(0)).build()).build()) {
				// Reading Records One by One in a String array
				String[] userFields;
				while ((userFields = csvReader.readNext()) != null) {
					if (userFields.length <= 1) {
						continue;
					}
					if (userFields.length < UserCsvFields.length()) {
						throw new InvalidCsvFile("Line '" + Arrays.toString(userFields) + "' is not well formed.");
					}
					CompanyUser companyUser = new CompanyUser(userFields[UserCsvFields.USERNAME.getCsvIndex()].trim(),
							userFields[UserCsvFields.PASSWORD.getCsvIndex()].trim(),
							createUniqueEmail(userFields[UserCsvFields.USERNAME.getCsvIndex()]).trim(), "", "",
							userFields[UserCsvFields.COMPANY.getCsvIndex()].trim(), userFields[UserCsvFields.FOLDER.getCsvIndex()]);
					users.add(companyUser);
				}
			}
		} catch (IOException e) {
			throw new InvalidCsvFile("Error reading the CSV file", e);
		}

		return users;
	}

	private static String createUniqueEmail(String userName) {
		return userName + "@test.com";
	}

}
