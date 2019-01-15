package com.biit.form.manager.folders;

import java.io.File;
import java.text.SimpleDateFormat;

import com.biit.form.entity.TreeObject;
import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.result.FormResult;

public class FileManager {
	private final static String PDF_BASIC_NAME = "formularioInicial.pdf";
	private final static String PDF_FILE_DATE_PATTERN = "yyMMdd";

	private final static String DOCUMENTATION_PREFIX = ".DOC.";

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PDF_FILE_DATE_PATTERN);

	public static String getPdfFileName(FormDescription formDescription) {
		return simpleDateFormat.format(formDescription.getCreationTime()) + PDF_BASIC_NAME;
	}

	public static String pdfFilePath(FormDescription formDescription) {
		return FolderManager.getPdfFormPath(formDescription.getUser()) + File.separator + FormManagerConfigurationReader.getInstance().getPdfStoredFolder()
				+ getPdfFileName(formDescription);
	}

	private static int getCategoryIndex(FormDescription formDescription, String categoryLabel) {
		FormResult formResult = FormResult.fromJson(formDescription.getJsonContent());
		for (TreeObject categoryResult : formResult.getChildren()) {
			if (categoryResult.getLabel().equalsIgnoreCase(categoryLabel)) {
				return formResult.getIndex(categoryResult);
			}
		}
		return 0;
	}

	public static String getDocumentationFolder(UploadedFile uploadedFile) {
		return String.format("%2d%s%s", getCategoryIndex(uploadedFile.getFormDescription(), uploadedFile.getCategory()), DOCUMENTATION_PREFIX, uploadedFile
				.getCategory().toUpperCase());
	}

	public static String attachedFilesPath(UploadedFile uploadedFile) {
		return FolderManager.getAttachedFilesRootPath(uploadedFile.getFormDescription().getUser()) + File.separator
				+ FormManagerConfigurationReader.getInstance().getAttachedFilesStoredFolder() + getDocumentationFolder(uploadedFile) + "";
	}

}
