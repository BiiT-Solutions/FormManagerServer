package com.biit.form.manager.form;

import com.biit.form.result.FormResult;
import com.biit.form.result.pdf.FormAsPdf;
import com.biit.form.result.pdf.exceptions.EmptyPdfBodyException;
import com.biit.form.result.pdf.exceptions.InvalidElementException;
import com.lowagie.text.DocumentException;

public class PdfConverter {

	public static byte[] convertToPdf(FormResult formResult, String footer) throws EmptyPdfBodyException, DocumentException, InvalidElementException {
		// Convert to pdf.
		FormAsPdf pdfDocument = new FormAsPdf(formResult, footer);
		return pdfDocument.generate();
	}
}
