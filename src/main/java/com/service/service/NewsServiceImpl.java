package com.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.service.entity.Article;
import com.service.entity.Translation;
import com.service.repository.ArticleRepository;
import com.service.util.ServiceContext;
import com.service.vo.NewsArticleVo;

@Service
public class NewsServiceImpl implements NewsService{
	
	private final ArticleRepository articleRepository;
	
	private final ServiceContext serviceContext;
	
	private final TranslationService translationService;

	public NewsServiceImpl(ArticleRepository articleRepository, ServiceContext serviceContext, 
					TranslationService translationService) {
		
		this.articleRepository = articleRepository;
		this.serviceContext = serviceContext;
		this.translationService = translationService;
	}

	@Override
	public List<NewsArticleVo> getAllNews() {
		
		List<Article> articles = articleRepository.findByLanguageAndLocationOrderByPublishedAtDesc(
				serviceContext.getLocalLanguage(), serviceContext.getUserContext().getLocation());
		
		return setTranslationArticles(articles);
	}

	@Override
	public List<NewsArticleVo> getNewsBySourceId(int sourceId) {
		
		 List<Article> articles = articleRepository.findBySourceIdAndLanguageAndLocationOrderByPublishedAtDesc(
				 sourceId, serviceContext.getLocalLanguage(), serviceContext.getUserContext().getLocation());

		 return setTranslationArticles(articles);
	}

	@Override
	public List<NewsArticleVo> getNewsByCategoryCode(String categoryCode) {
		
		 List<Article> articles = articleRepository.findByCategoryCodeAndLanguageAndLocationOrderByPublishedAtDesc(
				 categoryCode, serviceContext.getLocalLanguage(), serviceContext.getUserContext().getLocation());

		 return setTranslationArticles(articles);
	}
	
	private NewsArticleVo toResponse(Article article) {
		
		NewsArticleVo response = new NewsArticleVo();

	    response.setId(article.getId());
	    response.setTitle(article.getTitle());
	    response.setContent(article.getSummary());
	    response.setImageUrl(article.getImageUrl());
	    response.setLanguage(article.getLanguage());
	    response.setLocation(article.getLocation());
	    response.setCategory(article.getCategoryCode());
	    response.setSourceName(article.getSource().getName());
	    response.setSourceUrl(article.getSource().getUrl());
	    response.setPublishedAt(article.getPublishedAt());

	    return response;
	}
	
	private List<NewsArticleVo> setTranslationArticles(List<Article> articles) {
		
		for(Article article: articles) {
					
			Translation translatedArticle = translate(article, serviceContext.getUserContext().getLanguage());
			
			article.setTitle(translatedArticle.getTitle());
			article.setSummary(translatedArticle.getDescription());
		}

        return articles.stream()
                .map(article -> toResponse(article))
                .toList();
	}
	
	private Translation translate(Article article, String targetLanguage) {
		return translationService.translateArticle(article, targetLanguage);
	}
	
	public NewsArticleVo getArticleById(String articleId) {
		return toResponse(articleRepository.findById(articleId));
	}
}
