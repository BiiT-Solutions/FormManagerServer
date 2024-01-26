package com.biit.form.manager.form;

import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.folders.FolderManager;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.result.FormResult;
import com.biit.form.result.xls.FormsAsXls;
import com.biit.form.result.xls.exceptions.InvalidXlsElementException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;

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
            try {
                formResults.add(FormResult.fromJson(formDescription.getJsonContent()));
            } catch (JsonProcessingException e) {
                FormManagerLogger.errorMessage(FolderManager.class, e);
            }
            formHeaders.add(formDescription.getUser().getLoginName());
        }

        // Convert to PDF.
        byte[] pdfContent = XlsConverter.convertToXls(formResults, formHeaders);
        FormManagerLogger.info(XlsConverter.class.getName(), "XLS for '" + formDescriptions.size() + "' forms created correctly.");

        return pdfContent;
    }
}
