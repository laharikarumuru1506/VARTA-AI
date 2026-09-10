package com.service.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.exception.NewsProviderException;

@Service
public class SarvamTranslatorService implements TranslatorService{
	
	private final ObjectMapper objectMapper;

	SarvamTranslatorService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public String translate(String text, String sourceLanguage, String targetLanguage) {
		
		try {
			
			HttpResponse<String> response = sendRequest(text, sourceLanguage, targetLanguage);
			
			JsonNode root = objectMapper.readTree(response.body());

	        return root.path("translated_text").asText();
			
		} catch(Exception e) {
			throw new NewsProviderException("Failed to translate content", e);
		}
	}
	
	private HttpResponse<String> sendRequest(String text, String sourceLanguage, String targetLanguage){
	
		try {
			
			HttpClient httpClient = HttpClient.newBuilder()
											.followRedirects(HttpClient.Redirect.NORMAL)
											.build();

			String requestBody = """
					{
						"input" : "%s",
						"source_language_code" : "%s",
						"target_language_code" : "%s",
						"model" : "sarvam-translate:v1"
					}
						""".formatted(text.replace("\"", "\\\""), (sourceLanguage.toLowerCase() + "-IN"), (targetLanguage.toLowerCase() + "-IN"));
			
			HttpRequest request = HttpRequest.newBuilder()
										.uri(URI.create("https://api.sarvam.ai/translate"))
										.header("api-subscription-key", System.getenv("SARVAM_API_KEY"))
										.header("Content-Type", "application/json")
										.POST(HttpRequest.BodyPublishers.ofString(requestBody))
										.build();
			
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			
			if (response.statusCode() != 200) {
				throw new RuntimeException("Sarvam Translation API error: " + response.statusCode() + " - " + response.body());
			}
			
			return response;
			
		} catch(Exception e) {
			throw new RuntimeException("Failed to call Sarvam Translation API", e);
		}
	}
}
