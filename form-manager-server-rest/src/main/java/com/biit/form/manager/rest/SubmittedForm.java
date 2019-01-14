package com.biit.form.manager.rest;

import com.google.gson.annotations.SerializedName;

public class SubmittedForm {

	@SerializedName("name")
	private String name;
	@SerializedName("form_name")
	private String formName;
	@SerializedName("form_version")
	private String formVersion;
	@SerializedName("document")
	private String document;
	@SerializedName("application_name")
	private String applicationName;
	@SerializedName("json")
	private String json;

	public SubmittedForm() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(String formVersion) {
		this.formVersion = formVersion;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
