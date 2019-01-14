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
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.form.manager.entity.CompanyUser;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
	public CompanyUser login(@ApiParam(value = "", required = true) @RequestBody(required = true) LoginForm loginForm)
			throws InvalidUserException, InternalServerException {
		FormManagerLogger.info(this.getClass().getName(), "User " + loginForm.getUsername() + " succesfully logged in");
		if (loginForm.getUsername().equals("admin")) {
			FormManagerLogger.info(this.getClass().getName(),
					"User " + loginForm.getUsername() + " succesfully logged in");
			CompanyUser adminUser = new CompanyUser();
			adminUser.setLoginName("admin");
			adminUser.setFirstName("admin");
			FormManagerLogger.info(this.getClass().getName(), adminUser.toString());
			return adminUser;
			// return "{ \"user\": \"admin\", \"token\": \"wwwwwww\" }";
		} else {
			try {
				authenticationService.authenticate(loginForm.getUsername(), loginForm.getPassword());
			} catch (UserManagementException | AuthenticationRequired e) {
				FormManagerLogger.errorMessage(this.getClass().getName(), e);
				throw new InternalServerException(e);
			} catch (InvalidCredentialsException | UserDoesNotExistException e) {
				FormManagerLogger.warning(this.getClass().getName(),
						"Invalid user '" + loginForm.getUsername() + "' with password '" + loginForm.getPassword() + "'.");
				throw new InvalidUserException("Invalid user '" + loginForm.getUsername() + "' or password incorrect.", e);
			}
			throw new InvalidUserException("Invalid user '" + loginForm.getUsername() + "' or password incorrect.");
		}
	}
}
