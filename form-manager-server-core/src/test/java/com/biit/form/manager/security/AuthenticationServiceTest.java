package com.biit.form.manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.repository.IUserRepository;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

@SpringBootTest
@Rollback(true)
@Test(groups = "authentication")
public class AuthenticationServiceTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String USER_LOGIN = "oqueen";
	private final static String USER_PASSWORD = "arrow";
	private final static String USER_FIRSTNAME = "Oliver";
	private final static String USER_LASTNAME = "Queen";
	private final static String USER_EMAIL = "oqueen@starlingcity.com";

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IAuthenticationService<Long, Long> authenticationService;

	@Test(expectedExceptions = { InvalidCredentialsException.class })
	public void invalidUser() throws UserManagementException, AuthenticationRequired, InvalidCredentialsException, UserDoesNotExistException {
		authenticationService.addUser(null, USER_PASSWORD, USER_LOGIN, USER_EMAIL, null, USER_FIRSTNAME, null, USER_LASTNAME);
		authenticationService.authenticate(USER_LOGIN, USER_PASSWORD + "!");
	}

	@Test
	public void validUser() throws UserManagementException, AuthenticationRequired, InvalidCredentialsException, UserDoesNotExistException {
		authenticationService.addUser(null, USER_PASSWORD, USER_LOGIN, USER_EMAIL, null, USER_FIRSTNAME, null, USER_LASTNAME);

		IUser<Long> user = authenticationService.authenticate(USER_LOGIN, USER_PASSWORD);
		Assert.assertNotNull(user);
	}
}
