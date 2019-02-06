package com.biit.form.manager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

@Entity
@Primary
@Table(name = "form_descriptions")
public class FormDescription extends StoredFile {

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private CompanyUser user;

	@Lob
	@Column(name = "json_content", nullable = false, length = 10000000)
	private String jsonContent;

	@Lob
	@Column(name = "pdf_content", nullable = true, length = 10000000)
	private byte[] pdfContent;

	@Column(name = "document", nullable = false)
	private String document;

	@Column(name = "stored_in_nas", nullable = false)
	private boolean storedInNas = false;

	public FormDescription() {
		super();
	}

	public FormDescription(CompanyUser user, String jsonContent, String document) {
		this();
		setUser(user);
		setJsonContent(jsonContent);
		setDocument(document);
	}

	public CompanyUser getUser() {
		return user;
	}

	public void setUser(CompanyUser user) {
		this.user = user;
	}

	public String getJsonContent() {
		return jsonContent;
	}

	public void setJsonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}

	public byte[] getPdfContent() {
		return pdfContent;
	}

	public void setPdfContent(byte[] pdfContent) {
		this.pdfContent = pdfContent;
	}

	@Override
	public String toString() {
		return "Form ('" + getId() + "', '" + getUser() + "')";
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public boolean isStoredInNas() {
		return storedInNas;
	}

	public void setStoredInNas(boolean storedInNas) {
		this.storedInNas = storedInNas;
	}

}
