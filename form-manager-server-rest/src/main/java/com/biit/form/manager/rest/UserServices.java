package com.biit.form.manager.rest;

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
import com.biit.usermanager.repository.IUserRepository;

/**
 * Rest services for checking authorizations, permissions and logins.
 */
@RestController
public class UserServices {

	@Autowired
	private IUserRepository userRepository;

	@ApiOperation(value = "Basic method to add a new user to database.", notes = "Only one user can be added.")
	@RequestMapping(value = "/user/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addUser(@ApiParam(value = "User", required = true) @RequestBody(required = true) String requestBody) {

	}

	@ApiOperation(value = "Import a list of users created as CSV file", notes = "CSV structure as: Company; folder; username; password;")
	@RequestMapping(value = "/user/import", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void importUsers(@ApiParam(value = "CSV file", required = true) @RequestBody(required = true) String requestBody) throws InvalidUserException {
		try {
			List<CompanyUser> companyUsers = userRepository.save(UserFactory.createUsersFromCSV(requestBody));
			FormManagerLogger.info(this.getClass().getName(), "Added '" + companyUsers.size() + "' users to the database.");
		} catch (InvalidCsvFile e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidUserException("Invalid user creation.", e);
		}
	}
}
