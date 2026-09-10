package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer>{
	
	public List<Article> findByLanguageAndLocationOrderByPublishedAtDesc(String language, String location);

    public List<Article> findBySourceIdAndLanguageAndLocationOrderByPublishedAtDesc(int sourceId, String language, String location);

    public List<Article> findByCategoryCodeAndLanguageAndLocationOrderByPublishedAtDesc(String categoryCode, String language, String location);
    
    public Article findByOriginalUrl(String url);
    
    public Article findById(String Id);
}
