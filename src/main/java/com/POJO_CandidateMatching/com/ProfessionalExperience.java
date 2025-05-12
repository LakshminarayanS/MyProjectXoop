package com.POJO_CandidateMatching.com;

import java.util.List;

public class ProfessionalExperience {

	private String company;
	private String role;
	private String from_date;
	private String to_date;
	private List<String> key_responsibilities;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public List<String> getKey_responsibilities() {
		return key_responsibilities;
	}

	public void setKey_responsibilities(List<String> key_responsibilities) {
		this.key_responsibilities = key_responsibilities;
	}

}
