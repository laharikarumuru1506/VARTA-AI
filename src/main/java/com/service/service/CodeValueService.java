package com.service.service;

import java.util.List;

import com.service.entity.CodeValue;

public interface CodeValueService {
	
	public List<CodeValue> getByDomain(String domain);
}
