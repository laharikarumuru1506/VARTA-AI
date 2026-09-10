package com.service.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.service.vo.UserContext;

@Component
public class ServiceContext {
	
	private UserContext userContext;
	private String localLanguage;
	
	public ServiceContext() {}

	public UserContext getUserContext() {
		return userContext;
	}

	public void setUserContext(UserContext userContext) {
		this.userContext = userContext;
	}

	public String getLocalLanguage() {
		return localLanguage;
	}

	public void setLocalLanguage(String localLanguage) {
		this.localLanguage = localLanguage;
	}
}
