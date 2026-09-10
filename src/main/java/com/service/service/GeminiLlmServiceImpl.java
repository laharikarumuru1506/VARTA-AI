package com.service.service;

import org.springframework.stereotype.Service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service("GeminiService")
public class GeminiLlmServiceImpl implements LlmService{
	
	private final Client geminiClient;
	
	public GeminiLlmServiceImpl(Client geminiClient) {
		this.geminiClient = geminiClient;
	}

	@Override
	public String generateContent(String prompt) {
		
		GenerateContentResponse response = geminiClient.models.generateContent("gemini-3.6-flash", prompt, null);
		
		return response.text();
	}
}
