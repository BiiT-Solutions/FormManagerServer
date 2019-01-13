package com.biit.form.manager.entities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.usermanager.entities.User;
import com.biit.usermanager.repositories.IUserRepository;

@SpringBootTest
@Test(groups = "companyUserRepository")
@Rollback(true)
public class CompanyUserRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String USER_LOGIN = "oqueen";
	private final static String USER_PASSWORD = "arrow";
	private final static String USER_FIRSTNAME = "Oliver";
	private final static String USER_LASTNAME = "Queen";
	private final static String USER_EMAIL = "oqueen@starlingcity.com";
	// private final static String USER_NEW_EMAIL = "oqueen@lianyu.com";

	private final static String COMPANY = "Emin";
	private final static String FOLDER = "Folder";

	@Autowired
	private IUserRepository userRepository;

	@Test
	public void saveUser() {
		CompanyUser User = new CompanyUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		userRepository.save(User);

		List<User> users = userRepository.findAll();
		Assert.assertEquals(users.size(), 1);
		Assert.assertEquals(users.get(0).getFirstName(), USER_FIRSTNAME);
		Assert.assertEquals(users.get(0).getLastName(), USER_LASTNAME);
		Assert.assertEquals(users.get(0).getEmailAddress(), USER_EMAIL);
		Assert.assertEquals(((CompanyUser) users.get(0)).getCompany(), COMPANY);
		Assert.assertEquals(((CompanyUser) users.get(0)).getFolder(), FOLDER);
	}
}
