package com.biit.form.manager.folders;

import java.io.File;
import java.text.SimpleDateFormat;

import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.entity.FormDescription;

public class FileManager {
	private final static String PDF_BASIC_NAME = "formularioInicial.pdf";
	private final static String PDF_FILE_DATE_PATTERN = "yyMMdd";
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PDF_FILE_DATE_PATTERN);

	public static String getPdfFileName(FormDescription formDescription) {
		return simpleDateFormat.format(formDescription.getCreationTime()) + PDF_BASIC_NAME;
	}

	public static String pdfFilePath(FormDescription formDescription) {
		return FolderManager.getPdfFormPath(formDescription.getUser()) + File.separator + FormManagerConfigurationReader.getInstance().getPdfStoredFolder()
				+ getPdfFileName(formDescription);
	}
}
