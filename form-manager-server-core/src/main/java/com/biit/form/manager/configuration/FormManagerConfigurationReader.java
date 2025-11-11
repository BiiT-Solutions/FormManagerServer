package com.biit.form.manager.configuration;

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

import java.nio.file.Path;

import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;
import com.biit.utils.file.watcher.FileWatcher.FileModifiedListener;

public class FormManagerConfigurationReader extends ConfigurationReader {
	private static final String CONFIG_FILE = "settings.conf";
	private static final String SYSTEM_VARIABLE_CONFIG = "FORM_MANAGER_CONFIG";

	// Tags
	private static final String ID_FORMS_FILE_FOLDER = "forms.folder";
	private static final String DEFAULT_FORMS_FILE_FOLDER = "/mnt/nas/01.ABARCA_EXPEDIENTES";

	private static final String ID_FORMS_PDF_FILE_FOLDER = "forms.folder.pdf";
	private static final String DEFAULT_FORMS_PDF_FILE_FOLDER = "01.CHECKLIST";

	private static final String ID_FORMS_ATTACHED_FILES_FOLDER = "forms.folder.attached.files";
	private static final String DEFAULT_FORMS_ATTACHED_FILES_FOLDER = "02.DOC.ADJUNTA";

	private static final String ID_CSV_CHAR_SEPARATOR = "csv.separator";
	private static final String DEFAULT_CSV_CHAR_SEPARATOR = ";";

	private static final String ID_SEND_EMAIL_CONFIRMATION = "send.email.to";

	private static final String ID_WEBSERVICES_USER = "webservices.user";
	private static final String DEFAULT_WEBSERVICES_USER = "test";
	private static final String ID_WEBSERVICES_PASSWORD = "webservices.password";
	private static final String DEFAULT_WEBSERVICES_PASSWORD = "my-password";

	private static final String ID_STORE_IN_NAS = "store.in.nas";
	private static final String DEFAULT_STORE_IN_NAS = "true";

	private static final String ID_CREATE_USER_IF_NOT_EXISTS = "create.user.if.not.exists";
	private static final String DEFAULT_CREATE_USER_IF_NOT_EXISTS = "false";

	private static FormManagerConfigurationReader instance;

	private FormManagerConfigurationReader() {
		super();

		addProperty(ID_FORMS_FILE_FOLDER, DEFAULT_FORMS_FILE_FOLDER);
		addProperty(ID_FORMS_PDF_FILE_FOLDER, DEFAULT_FORMS_PDF_FILE_FOLDER);
		addProperty(ID_FORMS_ATTACHED_FILES_FOLDER, DEFAULT_FORMS_ATTACHED_FILES_FOLDER);
		addProperty(ID_CSV_CHAR_SEPARATOR, DEFAULT_CSV_CHAR_SEPARATOR);
		addProperty(ID_WEBSERVICES_USER, DEFAULT_WEBSERVICES_USER);
		addProperty(ID_WEBSERVICES_PASSWORD, DEFAULT_WEBSERVICES_PASSWORD);
		addProperty(ID_SEND_EMAIL_CONFIRMATION, "");
		addProperty(ID_STORE_IN_NAS, DEFAULT_STORE_IN_NAS);
		addProperty(ID_CREATE_USER_IF_NOT_EXISTS, DEFAULT_CREATE_USER_IF_NOT_EXISTS);

		PropertiesSourceFile sourceFile = new PropertiesSourceFile(CONFIG_FILE);
		sourceFile.addFileModifiedListeners(new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				FormManagerLogger.info(this.getClass().getName(), "Settings file '" + pathToFile + "' change detected.");
				readConfigurations();
			}
		});
		addPropertiesSource(sourceFile);
		SystemVariablePropertiesSourceFile systemSourceFile = new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE_CONFIG, CONFIG_FILE);
		systemSourceFile.addFileModifiedListeners(new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				FormManagerLogger.info(this.getClass().getName(), "System variable settings file '" + pathToFile + "' change detected.");
				readConfigurations();
			}
		});
		addPropertiesSource(systemSourceFile);

		readConfigurations();
	}

	public static FormManagerConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (FormManagerConfigurationReader.class) {
				if (instance == null) {
					instance = new FormManagerConfigurationReader();
				}
			}
		}
		return instance;
	}

	private String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	private String[] getPropertyCommaSeparatedValuesLogException(String propertyId) {
		try {
			return getCommaSeparatedValues(propertyId);
		} catch (PropertyNotFoundException e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	public String getFormsUrl() {
		return getPropertyLogException(ID_FORMS_FILE_FOLDER);
	}

	public String getPdfStoredFolder() {
		return getPropertyLogException(ID_FORMS_PDF_FILE_FOLDER);
	}

	public String getAttachedFilesStoredFolder() {
		return getPropertyLogException(ID_FORMS_ATTACHED_FILES_FOLDER);
	}

	public String getCsvSeparator() {
		return getPropertyLogException(ID_CSV_CHAR_SEPARATOR);
	}

	public String getWebservicesUser() {
		return getPropertyLogException(ID_WEBSERVICES_USER);
	}

	public String getWebservicesPassword() {
		return getPropertyLogException(ID_WEBSERVICES_PASSWORD);
	}

	public boolean isStoredInNasEnabled() {
		try {
			return Boolean.parseBoolean(getPropertyLogException(ID_STORE_IN_NAS));
		} catch (Exception e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			return false;
		}
	}

	public String[] getSendToEmails() {
		return getPropertyCommaSeparatedValuesLogException(ID_SEND_EMAIL_CONFIRMATION);
	}

	public boolean isCreateUserIfNotExistsEnabled() {
		try {
			return Boolean.parseBoolean(getPropertyLogException(ID_CREATE_USER_IF_NOT_EXISTS));
		} catch (Exception e) {
			FormManagerLogger.errorMessage(this.getClass().getName(), e);
			return false;
		}
	}
}
