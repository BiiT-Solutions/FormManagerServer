package com.biit.form.manager.rest;

/*-
 * #%L
 * Form Manager Server (Rest)
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

import com.biit.form.manager.repository.ICompanyUserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.manager.rest.exceptions.InvalidUserException;
import com.biit.form.manager.users.InvalidCsvFile;
import com.biit.form.manager.users.UserFactory;

/**
 * Rest services for checking authorizations, permissions and logins.
 */
@RestController
public class UserServices {

	@Autowired
	private ICompanyUserRepository companyUserRepository;

	@ApiOperation(value = "Basic method to add a new user to the database.", notes = "Only one user can be added.")
	@RequestMapping(value = "/user/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addUser(@ApiParam(value = "User", required = true) @RequestBody(required = true) String requestBody) {

	}

	@ApiOperation(value = "Import a list of users created as CSV file", notes = "CSV structure as: Company; folder; username; password;")
	@RequestMapping(value = "/user/import", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void importUsers(@ApiParam(value = "CSV file", required = true) @RequestBody(required = true) String requestBody) throws InvalidUserException {
		try {
			List<CompanyUser> companyUsers = companyUserRepository.saveAll(UserFactory.createUsersFromCSV(requestBody));
			FormManagerLogger.info(this.getClass().getName(), "Added '" + companyUsers.size() + "' users to the database.");
		} catch (InvalidCsvFile e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidUserException("Invalid user creation.", e);
		}
	}
}
