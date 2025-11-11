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

import javax.persistence.Entity;

import org.springframework.context.annotation.Primary;

import com.biit.usermanager.entity.User;

@Entity
@Primary
public class CompanyUser extends User {
	private String company;
	private String folder;

	public CompanyUser() {
		super();
	}

	public CompanyUser(String loginName, String password, String emailAddress, String firstName, String lastName, String company, String folder) {
		super(loginName, password, emailAddress, firstName, lastName);
		setCompany(company);
		setFolder(folder);
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company.trim();
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		if (folder != null) {
			this.folder = folder.trim();
		} else {
			this.folder = null;
		}
	}

	@Override
	public String toString() {
		return getUniqueName();
	}
}
