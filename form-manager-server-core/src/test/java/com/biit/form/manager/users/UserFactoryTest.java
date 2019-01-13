package com.biit.form.manager.users;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.usermanager.repository.IUserRepository;

@SpringBootTest
@Rollback(true)
@Test(groups = "userFactory")
public class UserFactoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String CSV_FILE = "usuarios.csv";
	private final static int TOTAL_USERS = 178;

	@Autowired
	private IUserRepository userRepository;

	@Test
	public void insertCsvFile() throws IOException, URISyntaxException, InvalidCsvFile {
		// Load form from json file in resources.
		String csvContent = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(CSV_FILE).toURI())));

		long totalStoredUsers = userRepository.count();

		List<CompanyUser> companyUsers = UserFactory.createUsersFromCSV(csvContent);
		Assert.assertEquals(companyUsers.size(), TOTAL_USERS);
		companyUsers = userRepository.save(companyUsers);
		Assert.assertEquals(companyUsers.size(), TOTAL_USERS);
		Assert.assertEquals(userRepository.count(), totalStoredUsers + TOTAL_USERS);

	}
}
