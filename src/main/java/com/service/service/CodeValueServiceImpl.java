package com.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.service.entity.CodeValue;
import com.service.repository.CodeValueRepository;

@Service
public class CodeValueServiceImpl implements CodeValueService{
	
	private final CodeValueRepository codeValueRepository;
	
	public CodeValueServiceImpl(CodeValueRepository codeValueRepository) {
		this.codeValueRepository = codeValueRepository;
	}

	@Override
	public List<CodeValue> getByDomain(String domain) {
		return codeValueRepository.findByDomain(domain);
	}
}
