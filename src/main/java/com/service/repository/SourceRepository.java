package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.entity.Source;

public interface SourceRepository extends JpaRepository<Source, Integer>{
	
	public List<Source> findByLanguage(String language);
}
