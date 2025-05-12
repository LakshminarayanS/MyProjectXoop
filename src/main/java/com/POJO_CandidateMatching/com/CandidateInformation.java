package com.POJO_CandidateMatching.com;

import java.util.List;

public class CandidateInformation {

	private String name;
	private String address;
	private String phone;
	private String email;
	private String linkedIn;
	private double total_year_experience;
	private List<String> domain_expertise;
	private List<String> professional_summary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkedIn() {
		return linkedIn;
	}

	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}

	public double getTotal_year_experience() {
		return total_year_experience;
	}

	public void setTotal_year_experience(double total_year_experience) {
		this.total_year_experience = total_year_experience;
	}

	public List<String> getDomain_expertise() {
		return domain_expertise;
	}

	public void setDomain_expertise(List<String> domain_expertise) {
		this.domain_expertise = domain_expertise;
	}

	public List<String> getProfessional_summary() {
		return professional_summary;
	}

	public void setProfessional_summary(List<String> professional_summary) {
		this.professional_summary = professional_summary;
	}

}
