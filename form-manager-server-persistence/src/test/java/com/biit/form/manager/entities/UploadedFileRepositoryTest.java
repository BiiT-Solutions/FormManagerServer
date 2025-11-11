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
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.manager.repository.IFormDescriptionRepository;
import com.biit.form.manager.repository.IUploadedFileRepository;
import com.biit.usermanager.repository.IUserRepository;

@SpringBootTest
@Test(groups = "uploadedFileRepository")
@Rollback(false)
public class UploadedFileRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private static final String FILE_CONTENT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dapibus, lorem et dignissim interdum, tortor leo fringilla erat, quis aliquam enim sapien id ligula. Etiam viverra condimentum orci suscipit malesuada. Cras sit amet nisl odio. Duis tellus ante, aliquam eu tellus non, tristique dignissim turpis. Aliquam sit amet commodo eros. Nullam molestie efficitur libero at malesuada. Suspendisse vel mattis est, non lacinia purus. Sed tincidunt commodo felis, eget condimentum risus porttitor sed. Cras risus velit, condimentum sit amet magna at, molestie dictum diam.";
	private static final String USER_LOGIN = "oqueen";
	private static final String USER_PASSWORD = "arrow";
	private static final String USER_FIRSTNAME = "Oliver";
	private static final String USER_LASTNAME = "Queen";
	private static final String USER_EMAIL = "oqueen@starlingcity.com";

	private static final String COMPANY = "Emin";
	private static final String FOLDER = "Folder";

	private static final String FORM_AS_JSON = "EminForm.json";

	private static final String CATEGORY_LABEL = "Categoría 1";
	private static final String FILE_NAME = "file.txt";

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IFormDescriptionRepository formDescriptionRepository;

	@Autowired
	private IUploadedFileRepository uploadedFileRepository;

	@Test
	public void saveUploadedFile() throws IOException, URISyntaxException {
		CompanyUser companyUser = new CompanyUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		CompanyUser user = userRepository.save(companyUser);

		// Load form from json file in resources.
		String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(FORM_AS_JSON).toURI())));
		FormDescription form = new FormDescription(user, text, "");
		FormDescription formDescription = formDescriptionRepository.save(form);

		UploadedFile uploadedFile = new UploadedFile(formDescription, FILE_CONTENT.getBytes(), CATEGORY_LABEL, FILE_NAME);
		uploadedFileRepository.save(uploadedFile);

		List<UploadedFile> uploadedFiles = uploadedFileRepository.findAll();
		Assert.assertEquals(uploadedFiles.size(), 1);
		Assert.assertEquals(uploadedFiles.get(0).getFormDescription(), formDescription);
		Assert.assertEquals(uploadedFiles.get(0).getCategory(), CATEGORY_LABEL);

		uploadedFiles = uploadedFileRepository.findByFormDescription(formDescription);
		Assert.assertEquals(uploadedFiles.size(), 1);
		Assert.assertEquals(uploadedFiles.get(0).getFormDescription(), formDescription);
		Assert.assertEquals(uploadedFiles.get(0).getCategory(), CATEGORY_LABEL);

		FormDescription form2 = new FormDescription(user, text, "");
		FormDescription formDescription2 = formDescriptionRepository.save(form2);
		uploadedFiles = uploadedFileRepository.findByFormDescription(formDescription2);
		Assert.assertEquals(uploadedFiles.size(), 0);

		uploadedFileRepository.deleteByFormDescription(formDescription);
		Assert.assertEquals(uploadedFileRepository.findAll().size(), 0);

	}
}
