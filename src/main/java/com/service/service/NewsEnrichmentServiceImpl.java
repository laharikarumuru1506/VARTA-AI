package com.service.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.vo.EnrichedNews;
import com.service.vo.NewsItem;

@Service
public class NewsEnrichmentServiceImpl implements NewsEnrichmentService{
	
	private final LlmService llmService;
	
	private final ObjectMapper objectMapper;

	public NewsEnrichmentServiceImpl(@Qualifier("GroqService") LlmService llmService, ObjectMapper objectMapper) {
		
		this.llmService = llmService;
		this.objectMapper = objectMapper;
	}

	@Override
	public EnrichedNews enrich(NewsItem newsItem) {
		
		String prompt = buildPrompt(newsItem);
		String response = llmService.generateContent(prompt);
		
		EnrichedNews enrichedNews = null;
		
		try {
			enrichedNews = objectMapper.readValue(response, EnrichedNews.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("LLM returned invalid JSON", e);
		}

		return enrichedNews;
	}
	
	private String buildPrompt(NewsItem newsItem) {
		return """
				You are a news analysis assistant.

				Analyze the provided news article and return structured information.
				
				Tasks:
				
				1. Identify the primary location associated with the main news event.
				2. Classify the article into exactly one category.
				3. Generate a concise summary.
				4. Extract important keywords.
				
				LOCATION RULES:

				Find the location where the MAIN NEWS EVENT actually happened.
				
				Follow this process before deciding the final city:
				
				1. Find the ORIGINAL LOCATION from the title and content.
				   Look for city, town, village, locality, suburb, mandal, district,
				   landmark, temple, hospital, etc.
				
				2. Resolve its geographic hierarchy:
				   Original location → Town/City → District → State → India.
				
				3. Verify which state the original location belongs to before selecting
				   the final city.
				
				4. Return the geographically appropriate CITY representing the event.
				   If the original place is a small locality/village/mandal, map it to its
				   actual nearby or administrative city only when the relationship is
				   reliably known.
				
				5. If multiple locations are mentioned, choose ONLY the location where
				   the main event occurred.
				
				6. NEVER use:
				   - News source/publisher location
				   - User's location
				   - Article language
				   - State capital by default
				   - Most famous city in the state without geographic evidence
				
				Example:
				"News from Alipiri, Tirupati" 
				→ Alipiri → Tirupati → Andhra Pradesh
				→ "Tirupati"
				
				"Kukatpally incident"
				→ Kukatpally → Hyderabad → Telangana
				→ "Hyderabad"
				
				If the event location cannot be reliably determined, return null.
				
				Return ONLY the city name.
				Do not return state, country, district, explanation, or multiple locations.
				
				CATEGORY RULES:
				
				Choose exactly ONE of these categories:
				
				LOCAL
				NATIONAL
				INTERNATIONAL
				POLITICS
				SPORTS
				BUSINESS
				ENTERTAINMENT
				TECHNOLOGY
				EDUCATION
				HEALTH
				WEATHER
				OTHER
				
				Return the category that best represents the main topic of the article.
				
				SUMMARY RULES:
				
				- Summarize the main event concisely.
				- Preserve the meaning of the original article.
				- Do not add information that is not present in the article.
				
				KEYWORD RULES:
				
				- Extract 3 to 8 important keywords.
				- Keywords must be directly relevant to the article.
				- Do not invent keywords.
				
				OUTPUT FORMAT:
				
				Return ONLY valid JSON.
				
				Use exactly this structure:
				
				{
				  "location": "Hyderabad",
				  "category": "LOCAL",
				  "summary": "Concise summary of the article.",
				  "keywords": ["keyword1", "keyword2", "keyword3"]
				}
				
				If the location cannot be reliably determined:
				
				{
				  "location": null,
				  "category": "OTHER",
				  "summary": "Concise summary of the article.",
				  "keywords": ["keyword1", "keyword2", "keyword3"]
				}
				
				Do not include markdown.
				Do not include ```json.
				Do not include explanations.
				Do not include additional fields.
				
				Return ONLY valid JSON.

				Do not use markdown.
				Do not add ```json.
				Do not add explanations.
				Ensure every string is properly enclosed in double quotes.
				Escape any double quotes inside string values.
				
				Use exactly this JSON structure:
				{
				  "summary": "string",
				  "category": "string",
				  "location": "string",
				  "keywords": ["string"]
				}
				
				Title:%s
				Content:%s	
							
				""".formatted(
						newsItem.getTitle(),
						newsItem.getSummary()
			);
	}
}
