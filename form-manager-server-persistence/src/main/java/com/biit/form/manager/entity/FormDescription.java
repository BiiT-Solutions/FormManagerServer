package com.biit.form.manager.entity;

/*-
 * #%L
 * Form Manager Server (Persistence)
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
