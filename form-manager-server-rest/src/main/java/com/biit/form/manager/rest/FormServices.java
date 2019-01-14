package com.biit.form.manager.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;

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

import com.biit.form.manager.form.PdfConverter;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.manager.rest.exceptions.InvalidFormException;
import com.biit.form.manager.rest.exceptions.PdfNotGeneratedException;
import com.biit.form.result.FormResult;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lowagie.text.DocumentException;

@RestController
public class FormServices {

	@ApiOperation(value = "Basic method to save a form result from the formrunner.", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public byte[] saveFormResult(@ApiParam(value = "Form result", required = true) @RequestBody(required = true) String body) throws InvalidFormException,
			PdfNotGeneratedException {
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
			try {
				byte[] pdfContent = PdfConverter.convertToPdf(formResult);
				FormManagerLogger.info(this.getClass().getName(), "PDF for '" + submittedForm.getDocument() + "' created correctly.");
				return pdfContent;
			} catch (EmptyPdfBodyException | DocumentException | InvalidElementException e) {
				throw new PdfNotGeneratedException("Pdf creation error.", e);
			}

		} catch (JsonSyntaxException e) {
			throw new InvalidFormException("Structure invalid", e);
		}
	}

	private SubmittedForm parsePetition(String petition) throws JsonSyntaxException {
		FormManagerLogger.debug(this.getClass().getName(), petition);
		return new Gson().fromJson(petition, SubmittedForm.class);
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
