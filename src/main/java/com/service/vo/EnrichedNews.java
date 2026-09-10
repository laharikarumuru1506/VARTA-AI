package com.service.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public class EnrichedNews {
	
	private String category;
	
	@JsonAlias({"primary_location", "location"})
    private String location;
	
    private String summary;
    private List<String> keywords;
    
    public EnrichedNews() {}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
}
