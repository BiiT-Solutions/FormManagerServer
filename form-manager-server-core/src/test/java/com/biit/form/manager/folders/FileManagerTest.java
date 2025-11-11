package com.biit.form.manager.folders;

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

import java.io.File;
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
	private static final String[] FINAL_FOLDERS = { "01.DOC.PROMOTOR", "02.DOC.PROYECTO", "03.DOC.AVAL", "04.DOC.AMBIENTAL", "05.DOC.URBANISMO",
			"06.DOC.AUTORIZACIONES", "07.DOC.TERRENOS", "08.DOC.EVACUACIÓN" };

	private static final String[] CATEGORY_LABELS = { "Promotor", "Proyecto", "Aval", "Ambiental", "Urbanismo", "Autorizaciones", "Terrenos", "Evacuación" };

	private static final String FILE_CONTENT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam dapibus, lorem et dignissim interdum, tortor leo fringilla erat, quis aliquam enim sapien id ligula. Etiam viverra condimentum orci suscipit malesuada. Cras sit amet nisl odio. Duis tellus ante, aliquam eu tellus non, tristique dignissim turpis. Aliquam sit amet commodo eros. Nullam molestie efficitur libero at malesuada. Suspendisse vel mattis est, non lacinia purus. Sed tincidunt commodo felis, eget condimentum risus porttitor sed. Cras risus velit, condimentum sit amet magna at, molestie dictum diam.";
	private static final String USER_LOGIN = "oqueen";
	private static final String USER_PASSWORD = "arrow";
	private static final String USER_FIRSTNAME = "Oliver";
	private static final String USER_LASTNAME = "Queen";
	private static final String USER_EMAIL = "oqueen@starlingcity.com";

	private static final String COMPANY = "Emin";
	private static final String FOLDER = "Folder";
	private static final String DOCUMENT = "12345";

	private static final String FORM_AS_JSON = "EminForm.json";

	private static final String FILE_NAME = "file.txt";

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
			Assert.assertEquals(FolderManager.getDocumentationFolder(uploadedFiles.get(i)), FINAL_FOLDERS[i]);
		}
	}

	@Test
	public void createFolders() {
		String path = System.getProperty("java.io.tmpdir") + File.separator + "folder1" + File.separator + "folder2" + File.separator + "folder3";
		FolderManager.createDirectoryStructureIfNeeded(path);
		File file = new File(path);
		Assert.assertTrue(file.exists() && file.isDirectory());
	}
}
