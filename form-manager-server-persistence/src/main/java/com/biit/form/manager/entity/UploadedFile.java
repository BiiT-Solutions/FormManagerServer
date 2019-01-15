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
@Table(name = "uploaded_files")
public class UploadedFile extends StoredFile {

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private FormDescription formDescription;

	@Lob
	@Column(name = "content", nullable = false, length = 1000000000)
	private byte[] content;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "category", nullable = false)
	private String category;

	private UploadedFile() {
		super();
	}

	public UploadedFile(FormDescription formDescription, byte[] content, String category, String fileName) {
		this();
		setFormDescription(formDescription);
		setContent(content);
		setFileName(fileName);
		setCategory(category);
	}

	public FormDescription getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(FormDescription formDescription) {
		this.formDescription = formDescription;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
