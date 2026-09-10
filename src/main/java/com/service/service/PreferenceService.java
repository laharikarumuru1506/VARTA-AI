package com.service.service;

import org.springframework.stereotype.Service;

import com.service.vo.PreferenceRequest;
import com.service.vo.UserContext;

@Service
public class PreferenceService {
	
	private final UserContextService userContextService;

    public PreferenceService(UserContextService userContextService) {
        this.userContextService = userContextService;
    }

    public void savePreferences(PreferenceRequest preferenceRequest) {

        UserContext userContext = new UserContext();
        
        userContext.setLanguage(preferenceRequest.getLanguage());
        userContext.setLocation(preferenceRequest.getLocation());
        
        userContextService.setContext(userContext);
    }

    public UserContext getPreferences() {
        return userContextService.getCurrentContext();
    }
}
