package com.service.service;

import java.util.List;

import com.service.vo.NewsArticleVo;

public interface NewsService {
	
	public List<NewsArticleVo> getAllNews();

    public List<NewsArticleVo> getNewsBySourceId(int sourceId);

    public List<NewsArticleVo> getNewsByCategoryCode(String categoryCode);
    
    public NewsArticleVo getArticleById(String articleId);
}
