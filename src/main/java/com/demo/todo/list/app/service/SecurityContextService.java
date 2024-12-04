package com.demo.todo.list.app.service;

import com.demo.todo.list.app.entity.UserDocument;

public interface SecurityContextService {

    String getUserId();

    UserDocument getUser();
}
