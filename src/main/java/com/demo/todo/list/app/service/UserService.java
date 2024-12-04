package com.demo.todo.list.app.service;

import com.demo.todo.list.app.entity.UserDocument;

public interface UserService {

    UserDocument createOrUpdate(UserDocument userDocument);

    boolean existByEmail(String email);

    UserDocument findByEmail(String email);
}
