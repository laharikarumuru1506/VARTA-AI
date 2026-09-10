package com.service.service;

import com.service.vo.EnrichedNews;
import com.service.vo.NewsItem;

public interface NewsEnrichmentService {
	
	public EnrichedNews enrich(NewsItem newsItem);
}
