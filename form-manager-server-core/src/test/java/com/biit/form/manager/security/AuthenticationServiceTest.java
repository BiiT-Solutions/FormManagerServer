package com.biit.form.manager.security;

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

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.repository.IUserRepository;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest
@Test(groups = "authentication")
public class AuthenticationServiceTest extends AbstractTestNGSpringContextTests {
    private static final String USER_LOGIN = "oqueen";
    private static final String USER_PASSWORD = "arrow";
    private static final String USER_FIRSTNAME = "Oliver";
    private static final String USER_LASTNAME = "Queen";
    private static final String USER_EMAIL = "oqueen@starlingcity.com";

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAuthenticationService<Long, Long> authenticationService;

    @Test(expectedExceptions = {InvalidCredentialsException.class})
    public void invalidUser() throws UserManagementException, AuthenticationRequired, InvalidCredentialsException, UserDoesNotExistException {
        authenticationService.addUser(null, USER_PASSWORD, USER_LOGIN, USER_EMAIL, null, USER_FIRSTNAME, null, USER_LASTNAME);
        authenticationService.authenticate(USER_LOGIN, USER_PASSWORD + "!");
    }

    @Test(enabled = false)
    public void validUser() throws UserManagementException, AuthenticationRequired, InvalidCredentialsException, UserDoesNotExistException {
        authenticationService.addUser(null, USER_PASSWORD, USER_LOGIN, USER_EMAIL, null, USER_FIRSTNAME, null, USER_LASTNAME);

        IUser<Long> user = authenticationService.authenticate(USER_LOGIN, USER_PASSWORD);
        Assert.assertNotNull(user);
    }
}
