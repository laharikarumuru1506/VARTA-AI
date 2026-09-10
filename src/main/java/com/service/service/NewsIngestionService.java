package com.service.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.service.vo.NewsItem;
import com.service.entity.Article;
import com.service.entity.Source;
import com.service.repository.ArticleRepository;

@Service
public class NewsIngestionService {
	
	private final NewsProvider newsProvider;
	
	private final ArticleRepository articleRepository;

	public NewsIngestionService(NewsProvider newsProvider, ArticleRepository articleRepository) {
		this.newsProvider = newsProvider;
		this.articleRepository = articleRepository;
	}
	
	public void ingest(Source source) {
		
		List<NewsItem> rawArticles = newsProvider.fetchNews(source.getUrl());
		
		List<Article> processedArticles = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(rawArticles)) {
			
			for(NewsItem newsItem: rawArticles) {
				processedArticles.add(this.setNewsArticle(newsItem, source));
			}
			
			articleRepository.saveAll(processedArticles);
		}
	}
	
	private Article setNewsArticle(NewsItem newsItem, Source source) {
		
		Article article = new Article();
		
		article.setSource(source);
		article.setTitle(newsItem.getTitle());
		article.setSummary(newsItem.getSummary());
		article.setImageUrl(newsItem.getImageUrl());
		
		article.setLanguage(source.getLanguage());
		article.setLocation(newsItem.getLocation());
		article.setCategoryCode(newsItem.getCategoryCode());
		article.setOriginalUrl(newsItem.getUrl());
		
		article.setPublishedBy(newsItem.getPublishedBy() != null ? newsItem.getPublishedBy() : source.getName());
		article.setPublishedAt(newsItem.getPublishedAt());
		
		article.setCreatedAt(LocalDateTime.now());
		
		return article;
	}
}
