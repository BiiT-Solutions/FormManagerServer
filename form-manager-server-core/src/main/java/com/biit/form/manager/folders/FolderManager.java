package com.biit.form.manager.folders;

import java.io.File;

import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.entity.CompanyUser;

public class FolderManager {

	public static String getUserPath(CompanyUser user) {
		return FormManagerConfigurationReader.getInstance().getFormsUrl() + File.separator + user.getFolder();
	}

	public static String getPdfFormPath(CompanyUser user) {
		return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getPdfStoredFolder();
	}

	public static String getAttachedFilesRootPath(CompanyUser user) {
		return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getAttachedFilesStoredFolder();
	}

}
