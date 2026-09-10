package com.service.service;

import com.service.vo.UserContext;

public interface UserContextService {
	
	public void setContext(UserContext context);

    public UserContext getCurrentContext();

    public void clearContext();
}
