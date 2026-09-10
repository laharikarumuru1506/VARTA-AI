package com.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.entity.Translation;

public interface TranslationRepository extends JpaRepository<Translation, Integer>{
	
	public Optional<Translation> findByArticleIdAndTargetLanguage(int articleId, String targetLanguage);
}