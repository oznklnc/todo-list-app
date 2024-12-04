package com.demo.todo.list.app.mapper;

import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.request.UserRegisterRequest;
import com.demo.todo.list.app.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract UserDocument toUserDocument(UserRegisterRequest userRegisterRequest);

    public abstract UserResponse toUserResponse(UserDocument userDocument);

    @Named("encodePassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
