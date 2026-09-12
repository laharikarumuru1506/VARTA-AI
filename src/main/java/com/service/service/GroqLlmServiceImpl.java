package com.service.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.exception.NewsProviderException;

@Service("GroqService")
public class GroqLlmServiceImpl implements LlmService{
	
	private final ObjectMapper objectMapper;

	GroqLlmServiceImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String generateContent(String prompt) {
		
		try {
			
			for (int attempt = 1; attempt <= 3; attempt++) {
			    try {
			    	
			    	HttpResponse<String> response = getContent(prompt);
			    	
			    	if (response == null) {
			    	    System.out.println("Groq returned empty response");
			    	    return null;
			    	}
			    	
			    	JsonNode root = objectMapper.readTree(response.body());

			        return root.path("choices").get(0).path("message").path("content").asText();

			    } catch (RuntimeException e) {

			        if (e.getMessage().contains("429")) {
			            try {
			            	Thread.sleep(10000);
			            } catch (InterruptedException ex) {
			                Thread.currentThread().interrupt();
			            }
			        } else {
			            throw e;
			        }
			    }
			}
			throw new RuntimeException("Groq API failed after retries");
	        
		} catch(Exception e) {
			throw new NewsProviderException("Failed to generate content", e);
		}
	}
	
	private HttpResponse<String> getContent(String prompt){
		
		try {
			
			HttpClient httpClient = HttpClient.newBuilder()
			        .followRedirects(HttpClient.Redirect.NORMAL)
			        .build();
			
			String requestBody = """
					{
						"model" : "openai/gpt-oss-20b",
						"messages": [
						    {
						      "role": "user", 
						      "content": %s
						    }
						  ]
					}
					""".formatted(objectMapper.writeValueAsString(prompt));
			
			HttpRequest request = HttpRequest.newBuilder()
									.uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
									.header("Authorization", "Bearer " + System.getenv("GROQ_API_KEY"))
				                    .header("Content-Type", "application/json")
				                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
				                    .build();
			
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			
			if (response.statusCode() != 200) {
                throw new RuntimeException("Groq API error: " + response.statusCode() + " - " + response.body());
            }
			
			return response;
			
		} catch(Exception e) {
			throw new RuntimeException("Failed to call Groq API", e);
		}
	}
}
