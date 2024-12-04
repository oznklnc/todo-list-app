package com.demo.todo.list.app.service.impl;

import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import com.demo.todo.list.app.service.SecurityContextService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {

    @Override
    public String getUserId() {
        return getUserFromUserDetails()
                .map(UserDocument::getId)
                .orElse(null);
    }

    @Override
    public UserDocument getUser() {
        return getUserFromUserDetails()
                .orElse(null);
    }


    private Optional<UserDocument> getUserFromUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
               .map(Authentication::getPrincipal)
               .map(TodoListUserDetails.class::cast)
               .map(TodoListUserDetails::getUser);
    }
}
