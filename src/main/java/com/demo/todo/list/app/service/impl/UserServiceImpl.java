package com.demo.todo.list.app.service.impl;

import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.repository.UserRepository;
import com.demo.todo.list.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDocument createOrUpdate(UserDocument userDocument) {
        return userRepository.save(userDocument);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsUserDocumentsByEmail(email);
    }

    @Override
    public UserDocument findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }


}
