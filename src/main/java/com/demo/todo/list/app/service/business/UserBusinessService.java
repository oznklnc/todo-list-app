package com.demo.todo.list.app.service.business;

import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.mapper.UserMapper;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.model.request.UserLoginRequest;
import com.demo.todo.list.app.model.request.UserRegisterRequest;
import com.demo.todo.list.app.model.response.LoginResponse;
import com.demo.todo.list.app.model.response.UserResponse;
import com.demo.todo.list.app.security.jwt.JwtHelper;
import com.demo.todo.list.app.service.SecurityContextService;
import com.demo.todo.list.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserBusinessService extends BaseBusinessService {

    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public UserBusinessService(SecurityContextService securityContextService,
                               UserService userService,
                               JwtHelper jwtHelper,
                               UserMapper userMapper,
                               AuthenticationManager authenticationManager) {
        super(securityContextService);
        this.userService = userService;
        this.jwtHelper = jwtHelper;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    public UserResponse registerUser(UserRegisterRequest request) {
        if (userService.existByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS);
        }
        UserDocument userDocument = userMapper.toUserDocument(request);
        UserDocument save = userService.createOrUpdate(userDocument);
        return userMapper.toUserResponse(
                save
        );
    }

    public LoginResponse login(UserLoginRequest userLoginRequest) {
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        authentication = authenticationManager.authenticate(authentication);
        return LoginResponse.builder()
                .accessToken(jwtHelper.generateJwtToken(authentication))
                .build();
    }


    public UserResponse getCurrentUser() {
        UserDocument loggedInUser = getLoggedInUser();
        return Optional.ofNullable(loggedInUser)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    }

}
