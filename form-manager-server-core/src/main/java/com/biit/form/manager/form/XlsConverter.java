package com.biit.form.manager.form;

import java.util.List;

import com.biit.form.result.FormResult;
import com.biit.form.result.xls.FormsAsXls;
import com.biit.form.result.xls.exceptions.InvalidXlsElementException;

public class XlsConverter {

	public static byte[] convertToXls(List<FormResult> formResults, List<String> formHeaders) throws InvalidXlsElementException {
		// Convert to pdf.
		FormsAsXls xlsDocument = new FormsAsXls(formResults, formHeaders);
		return xlsDocument.generate();
	}
}
