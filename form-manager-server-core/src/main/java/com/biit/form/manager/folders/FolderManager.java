package com.biit.form.manager.folders;

import java.io.File;

import com.biit.form.entity.TreeObject;
import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.result.FormResult;

public class FolderManager {
	private final static String DOCUMENTATION_PREFIX = ".DOC.";

	public static String getUserPath(CompanyUser user) {
		return FormManagerConfigurationReader.getInstance().getFormsUrl() + File.separator + user.getFolder();
	}

	public static String getPdfFolderPath(CompanyUser user) {
		return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getPdfStoredFolder();
	}

	public static String getAttachedFilesRootPath(CompanyUser user) {
		return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getAttachedFilesStoredFolder();
	}

	public static String getDocumentationFolder(UploadedFile uploadedFile) {
		return String.format("%02d%s%s", getCategoryIndex(uploadedFile.getFormDescription(), uploadedFile.getCategory()), DOCUMENTATION_PREFIX, uploadedFile
				.getCategory().toUpperCase());
	}

	private static int getCategoryIndex(FormDescription formDescription, String categoryLabel) {
		FormResult formResult = FormResult.fromJson(formDescription.getJsonContent());
		for (TreeObject categoryResult : formResult.getChildren()) {
			if (categoryResult.getLabel().equalsIgnoreCase(categoryLabel)) {
				return formResult.getIndex(categoryResult) + 1;
			}
		}
		return -1;
	}

	public static void createDirectoryStructureIfNeeded(String path) {
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		FormManagerLogger.info("FolderManager", "Path to automatically create: " + path);
		File outFile = new File(path + "01.DOC.FILES/");
		outFile.mkdirs();
	}
}
