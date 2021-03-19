package com.seerpharma.workmate.payload.request;

import java.util.Set;

public class SignupRequest {

	private String username;
	private String email;
	private String password;
	private Set<String> roles;
	private String companyShortName;

	public SignupRequest(String username, String email, String password, Set<String> roles, String companyShortName) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.companyShortName = companyShortName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getCompanyShortName() {
		return companyShortName;
	}

	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}
}
