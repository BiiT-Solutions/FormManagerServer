package com.biit.form.manager.entities;

/*-
 * #%L
 * Form Manager Server (Persistence)
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.usermanager.entity.User;
import com.biit.usermanager.repository.IUserRepository;

@SpringBootTest
@Test(groups = "companyUserRepository")
@Rollback(true)
public class CompanyUserRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private static final String USER_LOGIN = "oqueen";
	private static final String USER_PASSWORD = "arrow";
	private static final String USER_FIRSTNAME = "Oliver";
	private static final String USER_LASTNAME = "Queen";
	private static final String USER_EMAIL = "oqueen@starlingcity.com";
	// private static final String USER_NEW_EMAIL = "oqueen@lianyu.com";

	private static final String COMPANY = "Emin";
	private static final String FOLDER = "Folder";

	@Autowired
	private IUserRepository userRepository;

	@Test
	public void saveUser() {
		CompanyUser user = new CompanyUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		userRepository.save(user);

		List<User> users = userRepository.findAll();
		Assert.assertEquals(users.size(), 1);
		Assert.assertEquals(users.get(0).getFirstName(), USER_FIRSTNAME);
		Assert.assertEquals(users.get(0).getLastName(), USER_LASTNAME);
		Assert.assertEquals(users.get(0).getEmailAddress(), USER_EMAIL);
		Assert.assertEquals(((CompanyUser) users.get(0)).getCompany(), COMPANY);
		Assert.assertEquals(((CompanyUser) users.get(0)).getFolder(), FOLDER);
		// return (CompanyUser) users.get(0);
	}

	@Test
	public void findByLoginName() {
		CompanyUser user = new CompanyUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		userRepository.save(user);

		Assert.assertNotNull(userRepository.findByLoginName(USER_LOGIN));
	}
}
