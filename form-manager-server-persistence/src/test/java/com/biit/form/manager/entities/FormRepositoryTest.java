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
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.repository.IFormDescriptionRepository;
import com.biit.form.result.FormResult;
import com.biit.form.result.pdf.FormAsPdf;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.biit.usermanager.repository.IUserRepository;
import com.lowagie.text.DocumentException;

@SpringBootTest
@Test(groups = "formRepository")
@Rollback(true)
public class FormRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private static final String USER_LOGIN = "oqueen";
	private static final String USER_PASSWORD = "arrow";
	private static final String USER_FIRSTNAME = "Oliver";
	private static final String USER_LASTNAME = "Queen";
	private static final String USER_EMAIL = "oqueen@starlingcity.com";

	private static final String COMPANY = "Emin";
	private static final String FOLDER = "Folder";

	private static final String FORM_AS_JSON = "EminForm.json";

	private static final String DOCUMENT = "12345";

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IFormDescriptionRepository formDescriptionRepository;

	@Test
	public void saveForm() throws IOException, URISyntaxException, EmptyPdfBodyException, DocumentException, InvalidElementException {
		CompanyUser companyUser = new CompanyUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		companyUser = userRepository.save(companyUser);

		// Load form from json file in resources.
		String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(FORM_AS_JSON).toURI())));
		FormDescription form = new FormDescription(companyUser, text, DOCUMENT);
		formDescriptionRepository.save(form);
		List<FormDescription> forms = formDescriptionRepository.findAll();
		Assert.assertEquals(forms.size(), 1);
		Assert.assertEquals(forms.get(0).getUser(), companyUser);

		// Convert to PDF.
		FormResult formResult = FormResult.fromJson(form.getJsonContent());
		Assert.assertNotNull(formResult);

		FormAsPdf pdfDocument = new FormAsPdf(formResult, "Footer");
		form.setPdfContent(pdfDocument.generate());

		// Update and include PDF.
		form = formDescriptionRepository.save(form);
		Assert.assertEquals(forms.size(), 1);
		Assert.assertNotNull(form.getPdfContent());

		Assert.assertNotNull(formDescriptionRepository.findByUser(companyUser));

		// Search by document
		Assert.assertNotNull(formDescriptionRepository.findTopByDocumentOrderByCreationTimeDesc(DOCUMENT));

		// Search by stored in NAS.
		Assert.assertEquals(formDescriptionRepository.findByStoredInNas(true).size(), 0);
		Assert.assertEquals(formDescriptionRepository.findByStoredInNas(false).size(), 1);

		// Search by creationTime
		Assert.assertEquals(formDescriptionRepository.findByCreationTimeGreaterThanOrderByCreationTimeAsc(new Timestamp(System.currentTimeMillis() + 10000))
				.size(), 0);
		Assert.assertEquals(formDescriptionRepository.findByCreationTimeGreaterThanOrderByCreationTimeAsc(new Timestamp(System.currentTimeMillis() - 10000))
				.size(), 1);

		Assert.assertEquals(
				formDescriptionRepository.findByCreationTimeBetweenOrderByCreationTimeAsc(new Timestamp(System.currentTimeMillis() + 10000),
						new Timestamp(System.currentTimeMillis() + 10000)).size(), 0);
		Assert.assertEquals(
				formDescriptionRepository.findByCreationTimeBetweenOrderByCreationTimeAsc(new Timestamp(System.currentTimeMillis() + 10000),
						new Timestamp(System.currentTimeMillis() - 10000)).size(), 0);
		Assert.assertEquals(
				formDescriptionRepository.findByCreationTimeBetweenOrderByCreationTimeAsc(new Timestamp(System.currentTimeMillis() - 10000),
						new Timestamp(System.currentTimeMillis() + 10000)).size(), 1);

		form.setStoredInNas(true);
		form = formDescriptionRepository.save(form);

		Assert.assertEquals(formDescriptionRepository.findByStoredInNas(true).size(), 1);
		Assert.assertEquals(formDescriptionRepository.findByStoredInNas(false).size(), 0);
	}

	@Test
	public void searchForm() {
		CompanyUser companyUser = new CompanyUser(USER_LOGIN + "_2", USER_PASSWORD + "_2", USER_EMAIL + "_2", USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		companyUser = userRepository.save(companyUser);
		Assert.assertTrue(formDescriptionRepository.findByUser(companyUser).isEmpty());
	}
}
