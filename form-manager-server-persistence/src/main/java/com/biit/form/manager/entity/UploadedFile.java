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
@Table(name = "uploaded_files")
public class UploadedFile {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private FormDescription formDescription;

	@Lob
	@Column(name = "content", nullable = true, length = 1000000000)
	private byte[] content;

	private UploadedFile() {

	}

	public UploadedFile(FormDescription formDescription, byte[] content) {
		this();
	}

	public Long getId() {
		return id;
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
}
