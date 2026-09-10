package com.service.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.service.NewsService;
import com.service.vo.NewsArticleVo;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/news-article")
public class NewsArticleController {
	
	private final NewsService newsService;
	
	public NewsArticleController(NewsService newsService) {
		this.newsService = newsService;
	}
	
	@GetMapping("/{articleId}")
	public NewsArticleVo getArticleById(@PathVariable String articleId){
		return newsService.getArticleById(articleId);
	}
}
