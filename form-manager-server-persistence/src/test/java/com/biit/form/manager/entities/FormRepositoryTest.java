package com.biit.form.manager.entities;

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
import com.biit.form.manager.repository.IFormDescriptionRepository;
import com.biit.form.result.FormResult;
import com.biit.form.result.pdf.FormAsPdf;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.biit.usermanager.entity.User;
import com.biit.usermanager.repository.IUserRepository;
import com.lowagie.text.DocumentException;

@SpringBootTest
@Test(groups = "formRepository")
@Rollback(true)
public class FormRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String USER_LOGIN = "oqueen";
	private final static String USER_PASSWORD = "arrow";
	private final static String USER_FIRSTNAME = "Oliver";
	private final static String USER_LASTNAME = "Queen";
	private final static String USER_EMAIL = "oqueen@starlingcity.com";

	private final static String COMPANY = "Emin";
	private final static String FOLDER = "Folder";

	private final static String FORM_AS_JSON = "EminForm.json";

	private CompanyUser user;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IFormDescriptionRepository formDescriptionRepository;

	@Test
	public void saveUser() {
		CompanyUser companyUser = new CompanyUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		userRepository.save(companyUser);

		List<User> users = userRepository.findAll();
		Assert.assertEquals(users.size(), 1);
		Assert.assertEquals(users.get(0).getFirstName(), USER_FIRSTNAME);
		Assert.assertEquals(users.get(0).getLastName(), USER_LASTNAME);
		Assert.assertEquals(users.get(0).getEmailAddress(), USER_EMAIL);
		Assert.assertEquals(((CompanyUser) users.get(0)).getCompany(), COMPANY);
		Assert.assertEquals(((CompanyUser) users.get(0)).getFolder(), FOLDER);
		user = (CompanyUser) users.get(0);
	}

	@Test
	public void saveForm() throws IOException, URISyntaxException, EmptyPdfBodyException, DocumentException, InvalidElementException {
		// Load form from json file in resources.
		String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(FORM_AS_JSON).toURI())));
		FormDescription form = new FormDescription(user, text);
		formDescriptionRepository.save(form);
		List<FormDescription> forms = formDescriptionRepository.findAll();
		Assert.assertEquals(forms.size(), 1);
		Assert.assertEquals(forms.get(0).getUser(), user);

		// Convert to PDF.
		FormResult formResult = FormResult.fromJson(form.getJsonContent());
		Assert.assertNotNull(formResult);

		FormAsPdf pdfDocument = new FormAsPdf(formResult);
		form.setPdfContent(pdfDocument.generate());

		// Update and include PDF.
		form = formDescriptionRepository.save(form);
		Assert.assertEquals(forms.size(), 1);
		Assert.assertNotNull(form.getPdfContent());
	}
}
