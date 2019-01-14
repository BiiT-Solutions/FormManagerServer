package com.biit.form.manager.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.form.PdfConverter;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.manager.repository.IFormDescriptionRepository;
import com.biit.form.manager.rest.exceptions.DatabaseException;
import com.biit.form.manager.rest.exceptions.InvalidFormException;
import com.biit.form.manager.rest.exceptions.InvalidUserException;
import com.biit.form.manager.rest.exceptions.PdfNotGeneratedException;
import com.biit.form.result.FormResult;
import com.biit.form.result.pdf.FormAsPdf;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.biit.usermanager.repository.IUserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lowagie.text.DocumentException;

@RestController
public class FormServices {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IFormDescriptionRepository formDescriptionRepository;

	@ApiOperation(value = "Basic method to save a form result from the formrunner.", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public byte[] saveFormResult(@ApiParam(value = "Form result", required = true) @RequestBody(required = true) String body) throws InvalidFormException,
			PdfNotGeneratedException, DatabaseException {
		FormManagerLogger.info(this.getClass().getName(), "Form posted.");
		try {
			SubmittedForm submittedForm = parsePetition(body);
			if (submittedForm == null) {
				throw new InvalidFormException("Form not correctly submitted.");
			}
			FormResult formResult = FormResult.fromJson(submittedForm.getJson());
			if (formResult == null) {
				throw new InvalidFormException("Form not found.");
			}

			// Convert to PDF.
			byte[] pdfContent;
			try {
				pdfContent = PdfConverter.convertToPdf(formResult);
				FormManagerLogger.info(this.getClass().getName(), "PDF for '" + submittedForm.getDocument() + "' created correctly.");
			} catch (EmptyPdfBodyException | DocumentException | InvalidElementException e) {
				throw new PdfNotGeneratedException("Pdf creation error.", e);
			}

			// Store it on database.
			try {
				storeOnDatabase(submittedForm);
			} catch (InvalidUserException | EmptyPdfBodyException | DocumentException | InvalidElementException e) {
				throw new DatabaseException("Form has not been stored into the database", e);
			}

			return pdfContent;

		} catch (JsonSyntaxException e) {
			throw new InvalidFormException("Structure invalid", e);
		}
	}

	private SubmittedForm parsePetition(String petition) throws JsonSyntaxException {
		FormManagerLogger.debug(this.getClass().getName(), petition);
		return new Gson().fromJson(petition, SubmittedForm.class);
	}

	private void storeOnDatabase(SubmittedForm submittedForm) throws InvalidUserException, EmptyPdfBodyException, DocumentException, InvalidElementException,
			InvalidFormException {
		// Get user.
		CompanyUser user = (CompanyUser) userRepository.findByLoginName(submittedForm.getName());
		if (user == null) {
			throw new InvalidUserException("No user exists with login name '" + submittedForm.getName() + "'.");
		}
		// Store form
		FormDescription formDescription = new FormDescription(user, submittedForm.getJson());

		// Convert to PDF.
		FormResult formResult = FormResult.fromJson(submittedForm.getJson());
		if (formResult == null) {
			throw new InvalidFormException("Structure invalid");
		}
		FormAsPdf pdfDocument = new FormAsPdf(formResult);
		formDescription.setPdfContent(pdfDocument.generate());

		formDescriptionRepository.save(formDescription);
	}

	@ApiOperation(value = "Method to upload a file received as a multipart request", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/upload/{user}/formId/{formId}/category/{categoryName}")
	// //new annotation since 4.3
	public String fileUpload(@PathVariable("user") String user, @PathVariable("formId") String formId, @PathVariable("categoryName") String categoryName,
			@RequestParam("file") MultipartFile file) {
		FormManagerLogger.info(this.getClass().getName(), "Recieving file for user " + user + " formId " + formId + " and category " + categoryName);
		if (file.isEmpty()) {
			// redirectAttributes.addFlashAttribute("message", "Please select a
			// file to
			// upload");
			return "File is empty";
		}
		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			FormManagerLogger.info(this.getClass().getName(), "File " + file.getOriginalFilename());
			// FormManagerLogger.info(this.getClass().getName(),
			// "Files recieved"+ bytes);
			// Path path = Paths.get(UPLOADED_FOLDER +
			// file.getOriginalFilename());
			// Files.write(path, bytes);

			// redirectAttributes.addFlashAttribute("message",
			// "You successfully uploaded '" + file.getOriginalFilename() +
			// "'");

		} catch (IOException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e.getMessage());
			e.printStackTrace();
		}

		return "File received";
	}

}
