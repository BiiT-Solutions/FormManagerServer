package com.biit.form.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

import com.biit.usermanager.entity.User;

@Entity
@Primary
@Table(name = "users")
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
		this.folder = folder.trim();
	}

	@Override
	public String toString() {
		return getUniqueName();
	}
}
