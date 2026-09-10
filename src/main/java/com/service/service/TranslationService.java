package com.service.service;

import com.service.entity.Article;
import com.service.entity.Translation;

public interface TranslationService {
	
	public Translation translateArticle(Article article, String targetLanguage);
}
