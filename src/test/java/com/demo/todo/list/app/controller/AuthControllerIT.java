package com.demo.todo.list.app.controller;

import com.couchbase.client.core.error.AuthenticationFailureException;
import com.demo.todo.list.app.annotation.WithMockTodoUser;
import com.demo.todo.list.app.base.IntegrationBaseTest;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.model.request.UserLoginRequest;
import com.demo.todo.list.app.model.request.UserRegisterRequest;
import com.demo.todo.list.app.model.response.LoginResponse;
import com.demo.todo.list.app.model.response.UserResponse;
import com.demo.todo.list.app.service.business.UserBusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerIT extends IntegrationBaseTest {

    @MockitoBean
    UserBusinessService userBusinessService;


    @Test
    void should_register_user() throws Exception {
        //given
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();


        UserResponse userResponse = UserResponse.builder()
                .id("1")
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        
        when(userBusinessService.registerUser(any(UserRegisterRequest.class))).thenReturn(userResponse);

        //when & then
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(userBusinessService).registerUser(any(UserRegisterRequest.class));
    }


    @Test
    void should_login() throws Exception {
        //given
        UserLoginRequest request = UserLoginRequest.builder()
                .username("john.doe@example.com")
                .password("password")
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("token")
                .build();

        when(userBusinessService.login(any(UserLoginRequest.class))).thenReturn(loginResponse);

        //when & then
        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponse)));
        verify(userBusinessService).login(any(UserLoginRequest.class));
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_get_current_user() throws Exception {
        //given
        UserResponse userResponse = UserResponse.builder()
                .id("1")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();
        when(userBusinessService.getCurrentUser()).thenReturn(userResponse);

        //when & then
        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));

        verify(userBusinessService).getCurrentUser();

    }


    @Test
    void should_not_register_because_of_bad_request() throws Exception {
        //given
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("john.doeexample.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        //when & then
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(userBusinessService);
    }

    @Test
    void should_not_register_because_of_api_exception() throws Exception {
        //given
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        doThrow(new ApiException(ErrorCode.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST)).when(userBusinessService).registerUser(any(UserRegisterRequest.class));

        //when & then
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_register_because_of_internal_exception() throws Exception {
        //given
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        doThrow(AuthenticationFailureException.class).when(userBusinessService).registerUser(any(UserRegisterRequest.class));

        //when & then
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void should_throw_method_not_supported_exception() throws Exception {
        //when & then
        mockMvc.perform(get("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void should_throw_unsupported_media_type() throws Exception {
        //when & then
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_PDF))
                .andExpect(status().isUnsupportedMediaType());
    }

}