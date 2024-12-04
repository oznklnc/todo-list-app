package com.demo.todo.list.app.controller;

import com.demo.todo.list.app.annotation.WithMockTodoUser;
import com.demo.todo.list.app.base.IntegrationBaseTest;
import com.demo.todo.list.app.model.request.ProjectRequest;
import com.demo.todo.list.app.model.response.ProjectResponse;
import com.demo.todo.list.app.model.response.ProjectWrapperResponse;
import com.demo.todo.list.app.service.business.ProjectBusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerIT extends IntegrationBaseTest {

    @MockitoBean
    ProjectBusinessService projectBusinessService;

    private static final String PROJECT_API = "/api/v1/project";
    private static final String PROJECT_ID = "1";

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_create_project() throws Exception {
        //given
        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("testProject")
                .description("testDescription")
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id("1")
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .build();
        when(projectBusinessService.createProject(any(ProjectRequest.class))).thenReturn(projectResponse);

        //when & then
        mockMvc.perform(post(PROJECT_API)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));

        verify(projectBusinessService).createProject(any(ProjectRequest.class));
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_get_project_by_id() throws Exception {
        //given
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(PROJECT_ID)
                .name("testProject")
                .description("testDescription")
                .build();
        when(projectBusinessService.getProjectById(PROJECT_ID)).thenReturn(projectResponse);

        //when & then
        mockMvc.perform(get(PROJECT_API + "/" + PROJECT_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));

        verify(projectBusinessService).getProjectById(PROJECT_ID);
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_update_project_by_id() throws Exception {
        //given
        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("testProject")
                .description("testDescription")
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id("1")
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .build();
        when(projectBusinessService.updateProject(any(), any(ProjectRequest.class))).thenReturn(projectResponse);

        //when & then
        mockMvc.perform(put(PROJECT_API + "/" + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectResponse)));

        verify(projectBusinessService).updateProject(anyString(), any(ProjectRequest.class));
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_delete_project_by_id() throws Exception {
        //when & then
        mockMvc.perform(delete(PROJECT_API + "/" + PROJECT_ID))
                .andExpect(status().isNoContent());

        verify(projectBusinessService).deleteProjectById(PROJECT_ID);
    }

    @Test
    @WithMockTodoUser(username = "john.doe@example.com", password = "password")
    void should_get_all_projects() throws Exception {
        //given
        ProjectWrapperResponse projectWrapperResponse = ProjectWrapperResponse.builder()
                .projects(List.of())
                .build();
        when(projectBusinessService.getAllProjects()).thenReturn(projectWrapperResponse);
        //when & then
        mockMvc.perform(get(PROJECT_API + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projectWrapperResponse)));

        verify(projectBusinessService).getAllProjects();
    }
}