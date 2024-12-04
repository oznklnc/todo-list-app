package com.demo.todo.list.app.model.security;

import com.demo.todo.list.app.entity.UserDocument;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class TodoListUserDetails implements UserDetails {

    private final UserDocument userDocument;

    public TodoListUserDetails(UserDocument userDocument) {
        this.userDocument = userDocument;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return userDocument.getPassword();
    }

    @Override
    public String getUsername() {
        return userDocument.getEmail();
    }

    public UserDocument getUser() {
        return userDocument;
    }
}
