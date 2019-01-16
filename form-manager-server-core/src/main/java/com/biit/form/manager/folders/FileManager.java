package com.biit.form.manager.folders;

import java.io.File;
import java.text.SimpleDateFormat;

import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;

public class FileManager {
	private final static String PDF_BASIC_NAME = "formularioInicial.pdf";
	private final static String PDF_FILE_DATE_PATTERN = "yyMMdd";

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PDF_FILE_DATE_PATTERN);

	public static String getPdfFileName(FormDescription formDescription) {
		return simpleDateFormat.format(formDescription.getCreationTime()) + PDF_BASIC_NAME;
	}

	public static String pdfFilePath(FormDescription formDescription) {
		return FolderManager.getPdfFolderPath(formDescription.getUser()) + File.separator + getPdfFileName(formDescription);
	}

	public static String attachedFilesPath(UploadedFile uploadedFile) {
		return FolderManager.getAttachedFilesRootPath(uploadedFile.getFormDescription().getUser()) + File.separator
				+ FolderManager.getDocumentationFolder(uploadedFile) + File.separator + uploadedFile.getFileName();
	}

}
