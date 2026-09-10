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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				
				- The location must represent WHERE THE MAIN NEWS EVENT OCCURRED.
				- Look carefully for city, town, village, locality, suburb, mandal, district, or other geographic references in both the title and content.
				- If a specific locality, suburb, village, mandal, or small town is mentioned, identify its nearest well-known/popular city in India when the relationship is reliably known.
				- Return ONLY the city name.
				- Do NOT return the state or country.
				- Examples:
				  - "Kukatpally" → "Hyderabad"
				  - "Madhapur" → "Hyderabad"
				  - "Gachibowli" → "Hyderabad"
				  - "Banjara Hills" → "Hyderabad"
				  - "Whitefield" → "Bengaluru"
				  - "Andheri" → "Mumbai"
				- If the article directly mentions a major city, return that city.
				- If multiple locations are mentioned, return the location where the main event happened, not locations mentioned only for background or comparison.
				- Do NOT use the news source's location as the article location.
				- Do NOT assume Hyderabad merely because the article is from a Telugu news source.
				- Do NOT infer a city from the user's location.
				- For state-level news, return a major city only when the article clearly associates the event with that city.
				- If the primary event location cannot be determined reliably from the article, return null.
				- Never invent a location.
				
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
				
				Title:%s
				Content:%s	
							
				""".formatted(
						newsItem.getTitle(),
						newsItem.getSummary()
			);
	}
}
