package com.demo.todo.list.app.service.business;

import com.demo.todo.list.app.base.UnitTest;
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
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class UserBusinessServiceTest extends UnitTest {

    @Mock
    SecurityContextService securityContextService;
    @Mock
    UserService userService;
    @Mock
    JwtHelper jwtHelper;
    @Mock
    UserMapper userMapper;
    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    UserBusinessService userBusinessService;

    @Mock
    Authentication authentication;

    @Test
    void should_register_user() {
        //given
        String email = "john@example.com";
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email(email)
                .build();
        UserDocument userDocument = UserDocument.builder()
                .build();
        UserResponse userResponse = UserResponse.builder()
                .build();

        when(userService.existByEmail(email)).thenReturn(false);
        when(userMapper.toUserDocument(userRegisterRequest)).thenReturn(userDocument);
        when(userService.createOrUpdate(userDocument)).thenReturn(userDocument);
        when(userMapper.toUserResponse(userDocument)).thenReturn(userResponse);

        //when
        UserResponse response = userBusinessService.registerUser(userRegisterRequest);

        //then
        assertThat(response).isNotNull();
        InOrder inOrder = inOrder(userService, userMapper);
        inOrder.verify(userService).existByEmail(anyString());
        inOrder.verify(userMapper).toUserDocument(userRegisterRequest);
        inOrder.verify(userService).createOrUpdate(userDocument);
        inOrder.verify(userMapper).toUserResponse(userDocument);

    }

    @Test
    void should_throw_api_exception_when_user_already_exists() {
        //given
        String email = "john@example.com";
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email(email)
                .build();

        when(userService.existByEmail(email)).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> userBusinessService.registerUser(userRegisterRequest))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.USER_ALREADY_EXISTS.getMessageCode());

        verifyNoInteractions(userMapper);
        verifyNoMoreInteractions(userService);

    }

    @Test
    void should_login() {
        //given
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .username("john@example.com")
                .password("password")
                .build();
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtHelper.generateJwtToken(authentication)).thenReturn("token");

        //when
        LoginResponse response = userBusinessService.login(userLoginRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("token");
        InOrder inOrder = inOrder(authenticationManager, jwtHelper);
        inOrder.verify(authenticationManager).authenticate(any());
        inOrder.verify(jwtHelper).generateJwtToken(authentication);
    }

    @Test
    void should_get_current_user() {
        //given
        UserDocument userDocument = UserDocument.builder().build();
        UserResponse userResponse = UserResponse.builder().build();
        when(securityContextService.getUser()).thenReturn(userDocument);
        when(userMapper.toUserResponse(userDocument)).thenReturn(userResponse);

        //when
        UserResponse currentUser = userBusinessService.getCurrentUser();

        //then
        assertThat(currentUser).isNotNull();
        assertThat(currentUser).isSameAs(userResponse);
        InOrder inOrder = inOrder(securityContextService, userMapper);
        inOrder.verify(securityContextService).getUser();
        inOrder.verify(userMapper).toUserResponse(userDocument);
    }
}