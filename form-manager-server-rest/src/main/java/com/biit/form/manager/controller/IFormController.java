package com.biit.form.manager.controller;

/*-
 * #%L
 * Form Manager Server (Rest)
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

import java.io.FileNotFoundException;
import java.io.IOException;

import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.manager.rest.SubmittedForm;
import com.biit.form.manager.rest.exceptions.FileNotUploadedException;
import com.biit.form.manager.rest.exceptions.InvalidFormException;
import com.biit.form.manager.rest.exceptions.InvalidUserException;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.lowagie.text.DocumentException;

public interface IFormController {

	FormDescription convert(SubmittedForm submittedForm) throws InvalidUserException, InvalidFormException, EmptyPdfBodyException, DocumentException,
			InvalidElementException;

	FormDescription storeOnDatabase(SubmittedForm submittedForm) throws InvalidUserException, EmptyPdfBodyException, DocumentException,
			InvalidElementException, InvalidFormException;

	void storePdfFormInFolder(FormDescription formDescription) throws FileNotFoundException, IOException;

	UploadedFile createUploadedFile(byte[] bytes, String fileName, String formId, String categoryLabel) throws FileNotUploadedException;

	UploadedFile storeOnDatabase(byte[] bytes, String fileName, String formId, String categoryLabel) throws FileNotUploadedException;

	void storeUploadedFileInFolder(UploadedFile uploadedFile) throws FileNotFoundException, IOException;

}
