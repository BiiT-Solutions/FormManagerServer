package com.biit.form.manager.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.controller.IFormController;
import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.manager.form.PdfConverter;
import com.biit.form.manager.form.XlsConverter;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.manager.repository.IFormDescriptionRepository;
import com.biit.form.manager.repository.IUploadedFileRepository;
import com.biit.form.manager.rest.exceptions.DatabaseException;
import com.biit.form.manager.rest.exceptions.FileNotUploadedException;
import com.biit.form.manager.rest.exceptions.InvalidFormException;
import com.biit.form.manager.rest.exceptions.InvalidInputDataException;
import com.biit.form.manager.rest.exceptions.InvalidUserException;
import com.biit.form.manager.rest.exceptions.NoDataException;
import com.biit.form.manager.rest.exceptions.PdfNotGeneratedException;
import com.biit.form.manager.rest.exceptions.UserDoesNotExistsException;
import com.biit.form.manager.rest.exceptions.XlsNotGeneratedException;
import com.biit.form.result.FormResult;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.biit.form.result.xls.exceptions.InvalidXlsElementException;
import com.biit.logger.mail.SendEmail;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.usermanager.repository.IUserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lowagie.text.DocumentException;

@RestController
public class FormServices {
	private final static String DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IFormDescriptionRepository formDescriptionRepository;

	@Autowired
	private IUploadedFileRepository uploadedFileRepository;

	@Autowired
	private IFormController formController;

	@ApiOperation(value = "Basic method to save a form result from the formrunner.", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public byte[] saveFormResultAsPdf(@ApiParam(value = "Form result", required = true) @RequestBody(required = true) String body) throws InvalidFormException,
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
				pdfContent = PdfConverter.convertToPdf(formResult, submittedForm.getName());
				FormManagerLogger.info(this.getClass().getName(), "PDF for '" + submittedForm.getDocument() + "' created correctly.");
			} catch (EmptyPdfBodyException | DocumentException | InvalidElementException e) {
				FormManagerLogger.errorMessage(this.getClass().getName(), e);
				throw new PdfNotGeneratedException("Pdf creation error.", e);
			}

			// Store it on database.
			try {
				FormDescription formDescription = formController.storeOnDatabase(submittedForm);
				formDescription.setStoredInNas(false);
				FormManagerLogger.info(this.getClass().getName(), "Form '" + formDescription + "' stored in database correctly!.");

				try {
					// Store file on NAS
					formController.storePdfFormInFolder(formDescription);
					sendEmails(submittedForm.getName());
					formDescription.setStoredInNas(true);
					formDescriptionRepository.save(formDescription);
					FormManagerLogger.info(this.getClass().getName(), "Form '" + formDescription + "' stored in NAS!.");
				} catch (IOException e) {
					throw new PdfNotGeneratedException("Pdf File not stored into the folder.", e);
				}

			} catch (InvalidUserException | EmptyPdfBodyException | DocumentException | InvalidElementException e) {
				FormManagerLogger.errorMessage(this.getClass().getName(), e);
				throw new DatabaseException("Form has not been stored into the database", e);
			}

			return pdfContent;

		} catch (JsonSyntaxException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidFormException("Structure invalidformDescriptionRepository", e);
		}
	}

	@ApiOperation(value = "Gets forms as a XLS file", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms/xls", method = RequestMethod.GET, produces = "application/xls;charset=UTF-8")
	public byte[] getFormResultAsXls() throws InvalidFormException, XlsNotGeneratedException {
		FormManagerLogger.info(this.getClass().getName(), "Retrieving XLS forms.");
		try {
			List<FormDescription> formDescriptions = formDescriptionRepository.findAll();
			return XlsConverter.convertToXls(formDescriptions);
		} catch (InvalidXlsElementException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new XlsNotGeneratedException("Xls creation error.", e);
		}
	}

	@ApiOperation(value = "Gets forms as a XLS file", notes = "Obtained from a starting date as 'yyyy-mm-dd'")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms/xls/from/{date}", method = RequestMethod.GET, produces = "application/xls;charset=UTF-8")
	public byte[] getFormResultAsXls(@PathVariable("date") String dateQuery) throws InvalidFormException, XlsNotGeneratedException, InvalidInputDataException,
			NoDataException {
		FormManagerLogger.info(this.getClass().getName(), "Retrieving XLS forms.");
		try {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			Date date = format.parse(dateQuery);
			List<FormDescription> formDescriptions = formDescriptionRepository
					.findByCreationTimeGreaterThanOrderByCreationTimeAsc(new Timestamp(date.getTime()));
			if (formDescriptions.isEmpty()) {
				throw new NoDataException("No forms found in this date ranges.");
			}
			return XlsConverter.convertToXls(formDescriptions);
		} catch (InvalidXlsElementException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new XlsNotGeneratedException("Xls creation error.", e);
		} catch (JsonSyntaxException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidFormException("Structure invalid!", e);
		} catch (ParseException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidInputDataException("Date '" + dateQuery + "' is invalid. User format '" + DATE_FORMAT + "'", e);
		}
	}

	@ApiOperation(value = "Gets forms as a XLS file in a date range", notes = "Obtained from a starting date as 'yyyy-mm-dd'")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms/xls/from/{startdate}/to/{enddate}", method = RequestMethod.GET, produces = "application/xls;charset=UTF-8")
	public byte[] getFormResultAsXls(@PathVariable("startdate") String startdate, @PathVariable("enddate") String enddate) throws InvalidFormException,
			XlsNotGeneratedException, InvalidInputDataException, NoDataException {
		FormManagerLogger.info(this.getClass().getName(), "Retrieving XLS forms.");
		try {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			Date dateStart, dateEnd;
			try {
				dateStart = format.parse(startdate);
			} catch (ParseException e) {
				FormManagerLogger.errorMessage(this.getClass().getName(), e);
				throw new InvalidInputDataException("Date '" + startdate + "' is invalid. User format '" + DATE_FORMAT + "'", e);
			}
			try {
				dateEnd = format.parse(enddate);
				// Set time to the end of day
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateEnd);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				dateEnd = calendar.getTime();
			} catch (ParseException e) {
				FormManagerLogger.errorMessage(this.getClass().getName(), e);
				throw new InvalidInputDataException("Date '" + enddate + "' is invalid. User format '" + DATE_FORMAT + "'", e);
			}
			List<FormDescription> formDescriptions = formDescriptionRepository.findByCreationTimeBetweenOrderByCreationTimeAsc(
					new Timestamp(dateStart.getTime()), new Timestamp(dateEnd.getTime()));
			if (formDescriptions.isEmpty()) {
				throw new NoDataException("No forms found in this date ranges.");
			}
			return XlsConverter.convertToXls(formDescriptions);
		} catch (InvalidXlsElementException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new XlsNotGeneratedException("Xls creation error.", e);
		} catch (JsonSyntaxException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidFormException("Structure invalid!", e);
		}

	}

	private SubmittedForm parsePetition(String petition) throws JsonSyntaxException {
		FormManagerLogger.debug(this.getClass().getName(), petition);
		return new Gson().fromJson(petition, SubmittedForm.class);
	}

	@ApiOperation(value = "Method to upload a file received as a multipart request", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/upload/{user}/formId/{formId}/category/{categoryLabel}")
	public void fileUpload(@PathVariable("user") String user, @PathVariable("formId") String formId, @PathVariable("categoryLabel") String categoryLabel,
			@RequestParam("file") MultipartFile file) throws FileNotUploadedException, InvalidFormException, DatabaseException {
		FormManagerLogger.info(this.getClass().getName(), "Recieving file for user '" + user + "', form '" + formId + "', category '" + categoryLabel
				+ "', and file '" + file.getOriginalFilename() + "'.");
		if (file.isEmpty()) {
			throw new FileNotUploadedException("File is empty!");
		}
		try {
			// Store it on database.
			UploadedFile uploadedFile = formController.storeOnDatabase(file.getBytes(), file.getOriginalFilename(), formId, categoryLabel);

			try {
				// Store file on NAS
				formController.storeUploadedFileInFolder(uploadedFile);
			} catch (IOException e) {
				// Set as not stored.
				FormDescription formDescription = formDescriptionRepository.findByDocument(uploadedFile.getFileName());
				if (formDescription != null) {
					formDescription.setStoredInNas(false);
					formDescriptionRepository.save(formDescription);
				}
				throw new FileNotUploadedException("Attached File '" + uploadedFile + "' not stored into the folder.", e);
			}

		} catch (FileNotUploadedException | IOException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			throw new DatabaseException("Form has not been stored into the database", e);
		}
	}

	@ApiOperation(value = "Method to delete a form from database", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@DeleteMapping("/forms/{formId}")
	public void deleteForm(@PathVariable("formId") String formId) throws InvalidFormException {
		try {
			Long id = Long.parseLong(formId);

			FormDescription formDescription = formDescriptionRepository.getOne(id);
			if (formDescription == null) {
				throw new InvalidFormException("No form exists with id '" + formId + "'.");
			}

			uploadedFileRepository.deleteByFormDescription(formDescription);
			formDescriptionRepository.delete(formDescription);
		} catch (NumberFormatException nfe) {
			throw new InvalidFormException("Invalid id '" + formId + "'.");
		}

	}

	@ApiOperation(value = "Method to force the storage on the NAS for one user.", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@PostMapping("/forms/nas/{user}")
	public void storeInNas(@PathVariable("user") String user) throws UserDoesNotExistsException, PdfNotGeneratedException, FileNotUploadedException {
		CompanyUser companyUser = (CompanyUser) userRepository.findByLoginName(user);
		if (companyUser == null) {
			throw new UserDoesNotExistsException("No user exists with login name '" + companyUser + "'.");
		}

		List<FormDescription> formDescriptions = formDescriptionRepository.findByUser(companyUser);
		for (FormDescription formDescription : formDescriptions) {
			// Store file on NAS
			try {
				formController.storePdfFormInFolder(formDescription);
				sendEmails(companyUser.getUniqueName());
			} catch (IOException e) {
				throw new PdfNotGeneratedException("Pdf File not stored into the folder.", e);
			}

			for (UploadedFile uploadedFile : uploadedFileRepository.findByFormDescription(formDescription)) {
				try {
					// Store file on NAS
					formController.storeUploadedFileInFolder(uploadedFile);
				} catch (IOException e) {
					throw new FileNotUploadedException("Attached File '" + uploadedFile + "' not stored into the folder.", e);
				}
			}

			formDescription.setStoredInNas(true);
			formDescriptionRepository.save(formDescription);

		}
	}

	@ApiOperation(value = "Method to force the storage of all missing forms in the NAS.", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms/nas", method = RequestMethod.POST, produces = "text/plain")
	public String storeInNas() throws UserDoesNotExistsException, PdfNotGeneratedException, FileNotUploadedException {
		StringBuilder stringBuilder = new StringBuilder();
		List<FormDescription> formDescriptions = formDescriptionRepository.findByStoredInNas(false);
		for (FormDescription formDescription : formDescriptions) {
			// Store file on NAS
			try {
				formController.storePdfFormInFolder(formDescription);
				sendEmails(formDescription.getUser().getUniqueName());
			} catch (IOException e) {
				throw new PdfNotGeneratedException("Pdf File not stored into the folder.", e);
			}

			for (UploadedFile uploadedFile : uploadedFileRepository.findByFormDescription(formDescription)) {
				try {
					// Store file on NAS
					formController.storeUploadedFileInFolder(uploadedFile);
				} catch (IOException e) {
					throw new FileNotUploadedException("Attached File '" + uploadedFile + "' not stored into the folder.", e);
				}
			}
			formDescription.setStoredInNas(true);
			formDescriptionRepository.save(formDescription);
			stringBuilder.append(formDescription.getUser().getUniqueName() + " (" + formDescription.getDocument() + "), ");
		}
		return stringBuilder.toString();
	}

	private void sendEmails(String userName) {
		for (String email : FormManagerConfigurationReader.getInstance().getSendToEmails()) {
			try {
				SendEmail.sendEmail(email, "Formulario recibido por parte de '" + userName + "'.", "El usuario '" + userName
						+ "' ha enviado un formulario y este ha sido guardado con éxito.");
			} catch (EmailNotSentException | InvalidEmailAddressException e) {
				FormManagerLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}
}
