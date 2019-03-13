package com.biit.form.manager.form;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.result.FormResult;
import com.biit.form.result.xls.FormsAsXls;
import com.biit.form.result.xls.exceptions.InvalidXlsElementException;

public class XlsConverter {

	public static byte[] convertToXls(List<FormResult> formResults, List<String> formHeaders) throws InvalidXlsElementException {
		// Convert to pdf.
		FormsAsXls xlsDocument = new FormsAsXls(formResults, formHeaders);
		return xlsDocument.generate();
	}

	public static byte[] convertToXls(List<FormDescription> formDescriptions) throws InvalidXlsElementException {
		List<FormResult> formResults = new ArrayList<>();
		List<String> formHeaders = new ArrayList<>();
		for (FormDescription formDescription : formDescriptions) {
			formResults.add(FormResult.fromJson(formDescription.getJsonContent()));
			formHeaders.add(formDescription.getUser().getLoginName());
		}

		// Convert to PDF.
		byte[] pdfContent = XlsConverter.convertToXls(formResults, formHeaders);
		FormManagerLogger.info(XlsConverter.class.getName(), "XLS for '" + formDescriptions.size() + "' forms created correctly.");

		return pdfContent;
	}
}
