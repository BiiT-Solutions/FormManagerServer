package com.biit.form.manager.form;

/*-
 * #%L
 * Form Manager Server (Core)
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
