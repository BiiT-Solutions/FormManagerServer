package com.biit.form.manager.folders;

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

import com.biit.form.entity.TreeObject;
import com.biit.form.manager.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.form.result.FormResult;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;

public class FolderManager {
    private static final String DOCUMENTATION_PREFIX = ".DOC.";

    public static String getUserPath(CompanyUser user) {
        return FormManagerConfigurationReader.getInstance().getFormsUrl() + File.separator + user.getFolder();
    }

    public static String getPdfFolderPath(CompanyUser user) {
        return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getPdfStoredFolder();
    }

    public static String getAttachedFilesRootPath(CompanyUser user) {
        return getUserPath(user) + File.separator + FormManagerConfigurationReader.getInstance().getAttachedFilesStoredFolder();
    }

    public static String getDocumentationFolder(UploadedFile uploadedFile) {
        return String.format("%02d%s%s", getCategoryIndex(uploadedFile.getFormDescription(), uploadedFile.getCategory()), DOCUMENTATION_PREFIX, uploadedFile
                .getCategory().toUpperCase());
    }

    private static int getCategoryIndex(FormDescription formDescription, String categoryLabel) {
        try {
            FormResult formResult = FormResult.fromJson(formDescription.getJsonContent());
            for (TreeObject categoryResult : formResult.getChildren()) {
                if (categoryResult.getLabel().equalsIgnoreCase(categoryLabel)) {
                    return formResult.getIndex(categoryResult) + 1;
                }
            }
        } catch (JsonProcessingException e) {
            FormManagerLogger.errorMessage(FolderManager.class, e);
        }
        return -1;
    }

    public static void createDirectoryStructureIfNeeded(String path) {
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        FormManagerLogger.info("FolderManager", "Path to automatically create: " + path);
        File outFile = new File(path);
        outFile.mkdirs();
    }
}
