package com.biit.form.manager.configuration;


import java.nio.file.Path;

import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;
import com.biit.utils.file.watcher.FileWatcher.FileModifiedListener;

public class FormManagerConfigurationReader extends ConfigurationReader {

	private static final String CONFIG_FILE = "settings.conf";
	private static final String FORM_MANAGER_SYSTEM_VARIABLE_CONFIG = "FORM_MANAGER_CONFIG";


	// Regex Tags
	private static final String ID_MACHINE_DOMAIN = "machine.domain";
	private static final String ID_STAGNANT_TIME = "stagnant";


	// Defaults
	private static final String DEFAULT_MACHINE_DOMAIN = "https://testing.biit-solutions.com";


	private static FormManagerConfigurationReader instance;

	private FormManagerConfigurationReader() {
		super();

		addProperty(ID_MACHINE_DOMAIN, DEFAULT_MACHINE_DOMAIN);
		
		PropertiesSourceFile sourceFile = new PropertiesSourceFile(CONFIG_FILE);
		
		sourceFile.addFileModifiedListeners(new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				FormManagerLogger.info(this.getClass().getName(), "WAR settings file '" + pathToFile + "' change detected.");
				readConfigurations();
			}
		});
		addPropertiesSource(sourceFile);
		
		SystemVariablePropertiesSourceFile systemSourceFile = new SystemVariablePropertiesSourceFile(FORM_MANAGER_SYSTEM_VARIABLE_CONFIG, CONFIG_FILE);
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

	public String getMachineDomain() {
		return getPropertyLogException(ID_MACHINE_DOMAIN);
	}
	
	public String getStagnantTime() {
		return getPropertyLogException(ID_STAGNANT_TIME);
	}
	

}

