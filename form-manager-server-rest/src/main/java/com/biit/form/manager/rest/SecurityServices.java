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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.manager.rest.exceptions.InternalServerException;
import com.biit.form.manager.rest.exceptions.InvalidUserException;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

/**
 * Rest services for checking authorizations, permissions and logins.
 */
@RestController
public class SecurityServices {

	@Autowired
	private IAuthenticationService<Long, Long> authenticationService;

	@ApiOperation(value = "Basic method to check if the server is online.", notes = "")
	@RequestMapping(value = "/security/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public IUser<Long> login(@ApiParam(value = "", required = true) @RequestBody(required = true) LoginForm loginForm) throws InvalidUserException,
			InternalServerException {
		try {
			return authenticationService.authenticate(loginForm.getUsername(), loginForm.getPassword());
		} catch (UserManagementException | AuthenticationRequired e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new InternalServerException(e);
		} catch (InvalidCredentialsException | UserDoesNotExistException e) {
			FormManagerLogger.warning(this.getClass().getName(), "Invalid user '" + loginForm.getUsername() + "' with password '" + loginForm.getPassword()
					+ "'.");
			throw new InvalidUserException("Invalid user '" + loginForm.getUsername() + "' or password incorrect.", e);
		}
	}
}
