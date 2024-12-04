package com.demo.todo.list.app.service.business;

import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.service.SecurityContextService;

public abstract class BaseBusinessService {

    private final SecurityContextService securityContextService;

    public BaseBusinessService(SecurityContextService securityContextService) {
        this.securityContextService = securityContextService;
    }

    protected String getLoggedInUserId() {
        return securityContextService.getUserId();
    }

    protected UserDocument getLoggedInUser() {
        return securityContextService.getUser();
    }
}
