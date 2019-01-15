package com.biit.form.manager.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.rest.SubmittedForm;
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

	void storePdfForm(FormDescription formDescription, String path) throws FileNotFoundException, IOException;

}
