package com.biit.form.manager.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.manager.rest.exceptions.InternalServerException;
import com.biit.form.manager.rest.exceptions.InvalidUserException;
import com.biit.usermanager.security.IAuthenticationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Rest services for checking authorizations, permissions and logins.
 */
@RestController
public class SecurityServices {

	@Autowired
	private IAuthenticationService<Long, Long> authenticationService;

	@ApiOperation(value = "Basic method to check if the server is online.", notes = "The password is send in plain text. It is recommended the use of HTTPS for security reasons.")
	@RequestMapping(value = "/security/user/{userName}/password/{password}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String authorization(
			@ApiParam(value = "UserName", required = true) @PathVariable(value = "userName", required = true) String userName,
			@ApiParam(value = "password", required = true) @PathVariable(value = "password", required = true) String password)
			throws InvalidUserException, InternalServerException {
		// try {

		if (userName == "admin") {
			return "{ user: req.body.username, token: 'wwwwwww' }";
		} else {
			throw new InvalidUserException("Invalid user '" + userName + "' or password incorrect.");
			// return authenticationService.authenticate(userName, password);
		}
		/*
		 * } catch (UserManagementException | AuthenticationRequired e) {
		 * FormManagerLogger.errorMessage(this.getClass().getName(), e); throw new
		 * InternalServerException(e); } catch (InvalidCredentialsException |
		 * UserDoesNotExistException e) {
		 * FormManagerLogger.warning(this.getClass().getName(), "Invalid user '" +
		 * userName + "' with password '" + password + "'."); throw new
		 * InvalidUserException("Invalid user '" + userName +
		 * "' or password incorrect.", e); }
		 */
	}

	@ApiOperation(value = "Basic method to check if the server is online.", notes = "")
	@RequestMapping(value = "/security/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String authorization2(
			@ApiParam(value = "", required = true) @RequestBody(required = true) LoginForm loginForm) throws InvalidUserException
{
		if (loginForm.getUsername().equals("admin")) {
			// System.out.println(loginForm.getUsername());
			return "{ \"user\": \"admin\", \"token\": \"wwwwwww\" }";
		} else {
			throw new InvalidUserException("Invalid user '" + loginForm.getUsername() + "' or password incorrect.");
		}
	}
}
