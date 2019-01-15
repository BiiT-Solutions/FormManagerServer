package com.biit.form.manager.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.manager.folders.FileManager;
import com.biit.form.manager.folders.FolderManager;
import com.biit.form.manager.repository.IFormDescriptionRepository;
import com.biit.form.manager.repository.IUploadedFileRepository;
import com.biit.form.manager.rest.SubmittedForm;
import com.biit.form.manager.rest.exceptions.FileNotUploadedException;
import com.biit.form.manager.rest.exceptions.InvalidFormException;
import com.biit.form.manager.rest.exceptions.InvalidUserException;
import com.biit.form.result.FormResult;
import com.biit.form.result.pdf.FormAsPdf;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.biit.usermanager.repository.IUserRepository;
import com.lowagie.text.DocumentException;

@Component
public class FormController implements IFormController {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IFormDescriptionRepository formDescriptionRepository;

	@Autowired
	private IUploadedFileRepository uploadedFileRepository;

	@Override
	public FormDescription convert(SubmittedForm submittedForm) throws InvalidUserException, InvalidFormException, EmptyPdfBodyException, DocumentException,
			InvalidElementException {
		CompanyUser user = (CompanyUser) userRepository.findByLoginName(submittedForm.getName());
		if (user == null) {
			throw new InvalidUserException("No user exists with login name '" + submittedForm.getName() + "'.");
		}
		// Store form
		FormDescription formDescription = new FormDescription(user, submittedForm.getJson(), submittedForm.getDocument());

		// Convert to PDF.
		FormResult formResult = FormResult.fromJson(submittedForm.getJson());
		if (formResult == null) {
			throw new InvalidFormException("Structure invalid");
		}
		FormAsPdf pdfDocument = new FormAsPdf(formResult);
		formDescription.setPdfContent(pdfDocument.generate());
		return formDescription;
	}

	@Override
	public UploadedFile createUploadedFile(byte[] bytes, String fileName, String formId, String categoryLabel) throws FileNotUploadedException {
		FormDescription formDescription = formDescriptionRepository.findByDocument(formId);
		if (formDescription == null) {
			throw new FileNotUploadedException("Form '" + formId + "' does not exists");
		}
		return new UploadedFile(formDescription, bytes, categoryLabel, fileName);
	}

	@Override
	public UploadedFile storeOnDatabase(byte[] bytes, String fileName, String formId, String categoryLabel) throws FileNotUploadedException {
		UploadedFile uploadedFile = createUploadedFile(bytes, fileName, formId, categoryLabel);
		return uploadedFileRepository.save(uploadedFile);
	}

	@Override
	public FormDescription storeOnDatabase(SubmittedForm submittedForm) throws InvalidUserException, EmptyPdfBodyException, DocumentException,
			InvalidElementException, InvalidFormException {
		FormDescription formDescription = convert(submittedForm);
		return formDescriptionRepository.save(formDescription);
	}

	@Override
	public void storePdfForm(FormDescription formDescription) throws FileNotFoundException, IOException {
		// Create directories
		if (formDescription == null || formDescription.getUser() == null) {
			throw new FileNotFoundException("File not correctly defined.");
		}
		FolderManager.createDirectoryStructureIfNeeded(FolderManager.getPdfFolderPath(formDescription.getUser()));

		// Store file
		String path = FileManager.pdfFilePath(formDescription);
		if (!path.endsWith(".pdf")) {
			path += ".pdf";
		}

		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(formDescription.getPdfContent());
		}
	}

	@Override
	public void storeUploadedFile(UploadedFile uploadedFile) throws FileNotFoundException, IOException {
		// Create directories
		if (uploadedFile == null || uploadedFile.getFormDescription() == null || uploadedFile.getFormDescription().getUser() == null) {
			throw new FileNotFoundException("File not correctly defined.");
		}
		FolderManager.createDirectoryStructureIfNeeded(FolderManager.getAttachedFilesRootPath(uploadedFile.getFormDescription().getUser()));

		// Store file
		String path = FileManager.getDocumentationFolder(uploadedFile);
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(uploadedFile.getContent());
		}
	}

}
