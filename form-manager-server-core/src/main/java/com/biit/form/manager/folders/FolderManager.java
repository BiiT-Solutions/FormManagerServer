package com.biit.form.manager.folders;

import java.io.File;

import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.logger.FormManagerLogger;

public class FolderManager {

	public static String getUserPath(CompanyUser user) {
		return FormManagerConfigurationReader.getInstance().getFormsUrl() + File.separator + user.getFolder();
	}

	public static String getPdfFolderPath(CompanyUser user) {
		return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getPdfStoredFolder();
	}

	public static String getAttachedFilesRootPath(CompanyUser user) {
		FormManagerLogger.info("FolderManager", "AttachedFilesRootPath: " + getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getAttachedFilesStoredFolder());
		return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getAttachedFilesStoredFolder();
	}

	public static void createDirectoryStructureIfNeeded(String path) {
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		File outFile = new File(path);
		outFile.mkdirs();
	}
}
