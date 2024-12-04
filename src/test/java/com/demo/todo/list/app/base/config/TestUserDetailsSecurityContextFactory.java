package com.demo.todo.list.app.base.config;

import com.demo.todo.list.app.annotation.WithMockTodoUser;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class TestUserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithMockTodoUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockTodoUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDocument userDocument = UserDocument.builder()
                .email(annotation.username())
                .password(annotation.password())
                .build();
        TodoListUserDetails userDetails = new TodoListUserDetails(userDocument);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, annotation.password(), List.of());
        context.setAuthentication(auth);

        return context;
    }
}
