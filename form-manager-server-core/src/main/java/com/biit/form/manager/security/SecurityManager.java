package com.biit.form.manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biit.usermanager.entity.security.AuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

@Service
public class SecurityManager implements ISecurityManager {

	@Autowired
	private AuthenticationService authenticationService;

	public void authentication(String userName, String password) throws UserManagementException, AuthenticationRequired, InvalidCredentialsException,
			UserDoesNotExistException {
		authenticationService.authenticate(userName, password);
	}
}
