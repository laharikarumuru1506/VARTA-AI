package com.service.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.service.entity.Article;
import com.service.entity.Translation;
import com.service.repository.TranslationRepository;

@Service
public class TranslationServiceImpl implements TranslationService{
	
	private final TranslatorService translatorService;
	
	private final TranslationRepository translationRepository;

	public TranslationServiceImpl(TranslatorService translatorService, TranslationRepository translationRepository) {
		
		this.translatorService = translatorService;
		this.translationRepository = translationRepository;
	}

	@Override
	public Translation translateArticle(Article article, String targetLanguage) {
		
		Optional<Translation> existingTranslatedArticles = translationRepository
									.findByArticleIdAndTargetLanguage(article.getId(), targetLanguage);
		
		if(existingTranslatedArticles.isPresent()) {
			
			return existingTranslatedArticles.get();
			
		} else {
			
			Translation translation = this.setTranslationData(article, targetLanguage);
			
			translationRepository.save(translation);
			return translation;
		}
	}
	
	private Translation setTranslationData(Article article, String targetLanguage) {
		
		Translation translation = new Translation();
		
		translation.setArticle(article);
		translation.setTargetLanguage(targetLanguage);
		
		String translatedTitle = translatorService.translate(article.getTitle(), article.getLanguage(), targetLanguage) ;
		String translatedSummary = translatorService.translate(article.getSummary(), article.getLanguage(), targetLanguage) ;
		
		translation.setTitle(translatedTitle);
		translation.setDescription(translatedSummary);
		translation.setCreatedAt(LocalDateTime.now());
		
		return translation;
	}
}
