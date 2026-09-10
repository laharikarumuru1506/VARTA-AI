package com.service.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.service.entity.Source;
import com.service.repository.SourceRepository;
import com.service.service.NewsIngestionService;

@Component
public class NewsIngestionScheduler {
	
	private final NewsIngestionService newsIngestionService;
	
	private final SourceRepository sourceRepository;

	NewsIngestionScheduler(NewsIngestionService newsIngestionService, SourceRepository sourceRepository) {
		this.newsIngestionService = newsIngestionService;
		this.sourceRepository = sourceRepository;
	}
	
	@Scheduled(fixedRate = 30 * 60 * 1000)
	public void fetchNews() {
		
		String language = "TE";
		
		List<Source> sources = sourceRepository.findByLanguage(language);
		
		for(Source source: sources) {
			newsIngestionService.ingest(source);
		}
	}
}
