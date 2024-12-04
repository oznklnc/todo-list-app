package com.demo.todo.list.app.security.exception.handling;

import com.demo.todo.list.app.base.UnitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


class TodoListAuthenticationEntryPointTest extends UnitTest {

    TodoListAuthenticationEntryPoint todoListAuthenticationEntryPoint;

    @BeforeEach
    void setUp() {
        todoListAuthenticationEntryPoint = new TodoListAuthenticationEntryPoint(new ObjectMapper());
    }

    @Test
    void should_commence() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/v1/todos");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new BadCredentialsException("Authentication error");
        //when
        todoListAuthenticationEntryPoint.commence(request, response, authException);

        //then
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getHeader("WWW-Authenticate")).isEqualTo("Bearer");
    }
}