package com.demo.todo.list.app.security.exception.handling;

import com.demo.todo.list.app.base.UnitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


class TodoListAccessDeniedHandlerTest extends UnitTest {

    TodoListAccessDeniedHandler todoListAccessDeniedHandler;

    @BeforeEach
    void setUp() {
        todoListAccessDeniedHandler = new TodoListAccessDeniedHandler(new ObjectMapper());
    }

    @Test
    void should_handle_access_denied() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/v1/todos");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException exception = new AccessDeniedException("Access Denied");

        //when
        todoListAccessDeniedHandler.handle(request, response, exception);

        //then
        assertThat(response.getStatus()).isEqualTo(403);
    }
}