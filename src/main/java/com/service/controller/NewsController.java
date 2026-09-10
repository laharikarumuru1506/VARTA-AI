package com.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.service.NewsService;
import com.service.vo.NewsArticleVo;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/news")
public class NewsController {
	
	private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }
	
	@GetMapping
    public List<NewsArticleVo> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/source/{sourceId}")
    public List<NewsArticleVo> getNewsBySource(@PathVariable int sourceId) {
        return newsService.getNewsBySourceId(sourceId);
    }

    @GetMapping("/category/{categoryCode}")
    public List<NewsArticleVo> getNewsByCategory(@PathVariable String categoryCode) {
        return newsService.getNewsByCategoryCode(categoryCode);
    }
}
