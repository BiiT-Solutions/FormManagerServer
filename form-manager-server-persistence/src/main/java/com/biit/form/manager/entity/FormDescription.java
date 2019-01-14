package com.biit.form.manager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

@Entity
@Primary
@Table(name = "forms")
public class FormDescription {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private CompanyUser user;

	@Lob
	@Column(name = "json_content", nullable = false, length = 10000000)
	private String jsonContent;

	@Lob
	@Column(name = "pdf_content", nullable = true, length = 10000000)
	private byte[] pdfContent;

	public FormDescription() {

	}

	public FormDescription(CompanyUser user, String jsonContent) {
		this();
		setUser(user);
		setJsonContent(jsonContent);
	}

	public Long getId() {
		return id;
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

}
