package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.entity.CodeValue;

public interface CodeValueRepository extends JpaRepository<CodeValue, Integer>{
	
	public List<CodeValue> findByDomain(String domain);
	
}
