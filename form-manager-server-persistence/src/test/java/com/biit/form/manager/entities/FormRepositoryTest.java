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
		FormDescription form = new FormDescription(companyUser, text);
		formDescriptionRepository.save(form);
		List<FormDescription> forms = formDescriptionRepository.findAll();
		Assert.assertEquals(forms.size(), 1);
		Assert.assertEquals(forms.get(0).getUser(), companyUser);

		// Convert to PDF.
		FormResult formResult = FormResult.fromJson(form.getJsonContent());
		Assert.assertNotNull(formResult);

		FormAsPdf pdfDocument = new FormAsPdf(formResult);
		form.setPdfContent(pdfDocument.generate());

		// Update and include PDF.
		form = formDescriptionRepository.save(form);
		Assert.assertEquals(forms.size(), 1);
		Assert.assertNotNull(form.getPdfContent());

		Assert.assertNotNull(formDescriptionRepository.findByUser(companyUser));
	}

	@Test
	public void searchForm() {
		CompanyUser companyUser = new CompanyUser(USER_LOGIN + "_2", USER_PASSWORD + "_2", USER_EMAIL + "_2", USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		companyUser = userRepository.save(companyUser);
		Assert.assertTrue(formDescriptionRepository.findByUser(companyUser).isEmpty());
	}
}
