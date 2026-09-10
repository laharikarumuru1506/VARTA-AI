package com.service.service;

import java.util.List;

import com.service.vo.NewsItem;

public interface NewsProvider {
	
	public List<NewsItem> fetchNews(String sourceUrl);
}
