package com.biit.form.manager.folders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;

@SpringBootTest
@Test(groups = "fileManager")
@Rollback(false)
public class FileManagerTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String[] FINAL_FOLDERS = { "01.DOC.PROMOTOR", "02.DOC.PROYECTO", "03.DOC.AVAL", "04.DOC.AMBIENTAL", "05.DOC.URBANISMO",
			"06.DOC.AUTORIZACIONES", "07.DOC.TERRENOS", "08.DOC.EVACUACIÓN" };

	private final static String[] CATEGORY_LABELS = { "Promotor", "Proyecto", "Aval", "Ambiental", "Urbanismo", "Autorizaciones", "Terrenos", "Evacuación" };

	private final static String FILE_CONTENT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dapibus, lorem et dignissim interdum, tortor leo fringilla erat, quis aliquam enim sapien id ligula. Etiam viverra condimentum orci suscipit malesuada. Cras sit amet nisl odio. Duis tellus ante, aliquam eu tellus non, tristique dignissim turpis. Aliquam sit amet commodo eros. Nullam molestie efficitur libero at malesuada. Suspendisse vel mattis est, non lacinia purus. Sed tincidunt commodo felis, eget condimentum risus porttitor sed. Cras risus velit, condimentum sit amet magna at, molestie dictum diam.";
	private final static String USER_LOGIN = "oqueen";
	private final static String USER_PASSWORD = "arrow";
	private final static String USER_FIRSTNAME = "Oliver";
	private final static String USER_LASTNAME = "Queen";
	private final static String USER_EMAIL = "oqueen@starlingcity.com";

	private final static String COMPANY = "Emin";
	private final static String FOLDER = "Folder";
	private final static String DOCUMENT = "12345";

	private final static String FORM_AS_JSON = "EminForm.json";

	private final static String FILE_NAME = "file.txt";

	private CompanyUser companyUser;
	private FormDescription formDescription;
	private List<UploadedFile> uploadedFiles = new ArrayList<>();

	@BeforeClass
	public void createEntities() throws IOException, URISyntaxException {
		companyUser = new CompanyUser(USER_LOGIN, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, COMPANY, FOLDER);
		String text = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(FORM_AS_JSON).toURI())));
		formDescription = new FormDescription(companyUser, text, DOCUMENT);
		for (int i = 0; i < CATEGORY_LABELS.length; i++) {
			uploadedFiles.add(new UploadedFile(formDescription, FILE_CONTENT.getBytes(), CATEGORY_LABELS[i], FILE_NAME));
		}
	}

	@Test
	public void checkCategoryFolder() {
		for (int i = 0; i < CATEGORY_LABELS.length; i++) {
			Assert.assertEquals(FileManager.getDocumentationFolder(uploadedFiles.get(i)), FINAL_FOLDERS[i]);
		}
	}
}
