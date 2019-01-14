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
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.manager.repository.IFormDescriptionRepository;
import com.biit.form.manager.repository.IUploadedFileRepository;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.biit.usermanager.entity.User;
import com.biit.usermanager.repository.IUserRepository;
import com.lowagie.text.DocumentException;

@SpringBootTest
@Test(groups = "uploadedFileRepository")
@Rollback(false)
public class UploadedFileRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String FILE_CONTENT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dapibus, lorem et dignissim interdum, tortor leo fringilla erat, quis aliquam enim sapien id ligula. Etiam viverra condimentum orci suscipit malesuada. Cras sit amet nisl odio. Duis tellus ante, aliquam eu tellus non, tristique dignissim turpis. Aliquam sit amet commodo eros. Nullam molestie efficitur libero at malesuada. Suspendisse vel mattis est, non lacinia purus. Sed tincidunt commodo felis, eget condimentum risus porttitor sed. Cras risus velit, condimentum sit amet magna at, molestie dictum diam.";
	private final static String USER_LOGIN = "oqueen";
	private final static String USER_PASSWORD = "arrow";
	private final static String USER_FIRSTNAME = "Oliver";
	private final static String USER_LASTNAME = "Queen";
	private final static String USER_EMAIL = "oqueen@starlingcity.com";

	private final static String COMPANY = "Emin";
	private final static String FOLDER = "Folder";

	private final static String FORM_AS_JSON = "EminForm.json";

	private CompanyUser user;
	private FormDescription formDescription;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IFormDescriptionRepository formDescriptionRepository;

	@Autowired
	private IUploadedFileRepository uploadedFileRepository;

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
		formDescription = forms.get(0);
	}

	@Test
	public void saveUploadedFile() {
		UploadedFile uploadedFile = new UploadedFile(formDescription, FILE_CONTENT.getBytes());
		uploadedFileRepository.save(uploadedFile);

		List<UploadedFile> uploadedFiles = uploadedFileRepository.findAll();
		Assert.assertEquals(uploadedFiles.size(), 1);
		Assert.assertEquals(uploadedFiles.get(0).getFormDescription(), formDescription);
	}
}
