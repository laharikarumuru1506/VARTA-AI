package com.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.entity.CodeValue;
import com.service.service.CodeValueService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/codevalue")
public class CodeValueController {
	
	private final CodeValueService codeValueService;
	
	public CodeValueController(CodeValueService codeValueService) {
		this.codeValueService = codeValueService;
	}
	
	@GetMapping(value = "/domain/{domain}")
	public List<CodeValue> getByDomain(@PathVariable String domain){
		return codeValueService.getByDomain(domain);
	}
}
