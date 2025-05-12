package com.POJO_CandidateMatching.com;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateProfile {

	private CandidateInformation candidate_information;
	private List<String> professional_summary;
	private List<Education> education;
	private List<String> skills;
	private List<String> interpersonal_strengths;
	private List<ProfessionalExperience> professional_experience;
	private List<Certifications> certifications;
	private List<Projects> projects;
	private List<Achievements> achievements;

	public CandidateInformation getCandidate_information() {
		return candidate_information;
	}

	public void setCandidate_information(CandidateInformation candidate_information) {
		this.candidate_information = candidate_information;
	}

	public List<String> getProfessional_summary() {
		return professional_summary;
	}

	public void setProfessional_summary(List<String> professional_summary) {
		this.professional_summary = professional_summary;
	}

	public List<Education> getEducation() {
		return education;
	}

	public void setEducation(List<Education> education) {
		this.education = education;
	}

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	public List<String> getInterpersonal_strengths() {
		return interpersonal_strengths;
	}

	public void setInterpersonal_strengths(List<String> interpersonal_strengths) {
		this.interpersonal_strengths = interpersonal_strengths;
	}

	public List<ProfessionalExperience> getProfessional_experience() {
		return professional_experience;
	}

	public void setProfessional_experience(List<ProfessionalExperience> professional_experience) {
		this.professional_experience = professional_experience;
	}

	public List<Certifications> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<Certifications> certifications) {
		this.certifications = certifications;
	}

	public List<Projects> getProjects() {
		return projects;
	}

	public void setProjects(List<Projects> projects) {
		this.projects = projects;
	}

	public List<Achievements> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<Achievements> achievements) {
		this.achievements = achievements;
	}

}
