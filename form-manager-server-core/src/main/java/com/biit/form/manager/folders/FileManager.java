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
import java.text.SimpleDateFormat;

import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;

public class FileManager {
	private static final String PDF_BASIC_NAME = "formularioInicial.pdf";
	private static final String PDF_FILE_DATE_PATTERN = "yyMMdd";

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
