package com.demo.todo.list.app.controller;

import com.demo.todo.list.app.annotation.WithMockTodoUser;
import com.demo.todo.list.app.base.IntegrationBaseTest;
import com.demo.todo.list.app.model.request.TodoRequest;
import com.demo.todo.list.app.model.response.TodoResponse;
import com.demo.todo.list.app.model.response.TodoWrapperResponse;
import com.demo.todo.list.app.service.business.TodoBusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodoControllerTest extends IntegrationBaseTest {

    @MockitoBean
    TodoBusinessService todoBusinessService;

    private static final String TODO_API = "/api/v1/todo";
    private static final String TODO_ID = "1";
    private static final String PROJECT_ID = "1";

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_create_todo() throws Exception {
        //given
        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();
        TodoResponse todoResponse = TodoResponse.builder()
                .id("1")
                .title(todoRequest.getTitle())
                .description(todoRequest.getDescription())
                .build();

        when(todoBusinessService.addTodo(any(TodoRequest.class))).thenReturn(todoResponse);

        //when & then
        mockMvc.perform(post(TODO_API)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(todoResponse)));

        verify(todoBusinessService).addTodo(any(TodoRequest.class));
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_create_sub_todo() throws Exception {
        //given
        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();
        TodoResponse todoResponse = TodoResponse.builder()
                .id("1")
                .title(todoRequest.getTitle())
                .description(todoRequest.getDescription())
                .build();

        when(todoBusinessService.addSubTodo(any(TodoRequest.class), anyString())).thenReturn(todoResponse);

        //when & then
        mockMvc.perform(post(TODO_API + "/" + TODO_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(todoResponse)));

        verify(todoBusinessService).addSubTodo(any(TodoRequest.class), anyString());
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_get_todo_by_id() throws Exception {
        //given
        TodoResponse todoResponse = TodoResponse.builder()
                .id(TODO_ID)
                .title("testTitle")
                .description("testDescription")
                .build();
        when(todoBusinessService.getTodoById(TODO_ID)).thenReturn(todoResponse);

        //when & then
        mockMvc.perform(get(TODO_API + "/" + TODO_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todoResponse)));

        verify(todoBusinessService).getTodoById(TODO_ID);
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_get_todos() throws Exception {
        //given
        TodoWrapperResponse todoWrapperResponse = TodoWrapperResponse.builder()
                .total(0)
                .todoResponses(List.of())
                .build();
        when(todoBusinessService.getTodos(PROJECT_ID)).thenReturn(todoWrapperResponse);

        //when & then
        mockMvc.perform(get(TODO_API)
                        .param("projectId", PROJECT_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todoWrapperResponse)));

        verify(todoBusinessService).getTodos(PROJECT_ID);
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_delete_todo_by_id() throws Exception {
        //when & then
        mockMvc.perform(delete(TODO_API + "/" + TODO_ID))
                .andExpect(status().isNoContent());

        verify(todoBusinessService).deleteTodoById(TODO_ID);
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_update_todo_by_id() throws Exception {
        //given
        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();
        TodoResponse todoResponse = TodoResponse.builder()
                .id("1")
                .title(todoRequest.getTitle())
                .description(todoRequest.getDescription())
                .build();
        when(todoBusinessService.updateTodoById(eq(TODO_ID), any(TodoRequest.class))).thenReturn(todoResponse);

        //when & then
        mockMvc.perform(put(TODO_API + "/" + TODO_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(todoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todoResponse)));

        verify(todoBusinessService).updateTodoById(eq(TODO_ID), any(TodoRequest.class));
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_complete_todo_by_id() throws Exception {
        //when & then
        mockMvc.perform(put(TODO_API + "/" + TODO_ID + "/complete"))
                .andExpect(status().isNoContent());

        verify(todoBusinessService).updateAsCompleted(TODO_ID);
    }
}