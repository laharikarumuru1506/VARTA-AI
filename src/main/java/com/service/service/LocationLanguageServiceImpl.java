package com.service.service;

import org.springframework.stereotype.Service;

@Service
public class LocationLanguageServiceImpl implements LocationLanguageService{

	@Override
	public String getLocalLanguage(String location) {
		
		String language = "TE";
		
		return language;
	}
}
