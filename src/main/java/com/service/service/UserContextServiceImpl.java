package com.service.service;

import org.springframework.stereotype.Service;

import com.service.util.ServiceContext;
import com.service.vo.UserContext;

import jakarta.servlet.http.HttpSession;

@Service
public class UserContextServiceImpl implements UserContextService{
	
	private static final String CONTEXT_KEY = "USER_CONTEXT";
	
	private final HttpSession session;
	
	private final ServiceContext serviceContext;
	
	private final LocationLanguageService locationLanguageService;

    public UserContextServiceImpl(HttpSession session, ServiceContext serviceContext, 
    				LocationLanguageService locationLanguageService) {
    	
        this.session = session;
		this.serviceContext = serviceContext;
		this.locationLanguageService = locationLanguageService;
    }

	@Override
	public void setContext(UserContext context) {
		
		session.setAttribute(CONTEXT_KEY, context);
		
		serviceContext.setUserContext(context);
		serviceContext.setLocalLanguage(locationLanguageService.getLocalLanguage(context.getLocation()));
	}

	@Override
	public UserContext getCurrentContext() {
		
		UserContext context = (UserContext) session.getAttribute(CONTEXT_KEY);

        if (context == null) {
            throw new IllegalStateException("User preferences are not configured");
        }

        return context;
	}

	@Override
	public void clearContext() {
		
		session.removeAttribute(CONTEXT_KEY);
		
		serviceContext.setUserContext(null);
		serviceContext.setLocalLanguage(null);
	}
}
