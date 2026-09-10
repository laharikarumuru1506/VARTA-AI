package com.service.vo;

public class PreferenceRequest {
	
	private String language;
	private String location;
	
	public PreferenceRequest() {}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
