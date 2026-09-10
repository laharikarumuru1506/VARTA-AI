package com.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.genai.Client;

@Configuration
public class GeminiConfig {
	
	@Bean 
	Client geminiClient() {
        return Client.builder().apiKey(System.getenv("GEMINI_API_KEY")).build();
    }
}
