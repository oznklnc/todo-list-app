package com.demo.todo.list.app.base;

import com.demo.todo.list.app.annotation.IntegrationTest;
import com.demo.todo.list.app.repository.ProjectRepository;
import com.demo.todo.list.app.repository.TodoRepository;
import com.demo.todo.list.app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
public class IntegrationBaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected UserRepository userRepository;

    @MockitoBean
    protected ProjectRepository projectRepository;

    @MockitoBean
    protected TodoRepository todoRepository;
}
