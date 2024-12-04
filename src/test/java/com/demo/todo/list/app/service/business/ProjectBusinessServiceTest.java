package com.demo.todo.list.app.service.business;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.ProjectDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.mapper.ProjectMapper;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.model.request.ProjectRequest;
import com.demo.todo.list.app.model.response.ProjectResponse;
import com.demo.todo.list.app.model.response.ProjectWrapperResponse;
import com.demo.todo.list.app.service.ProjectService;
import com.demo.todo.list.app.service.SecurityContextService;
import com.demo.todo.list.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;


class ProjectBusinessServiceTest extends UnitTest {

    @Mock
    SecurityContextService securityContextService;
    @Mock
    TodoService todoService;
    @Mock
    ProjectService projectService;
    @Mock
    ProjectMapper projectMapper;
    @InjectMocks
    ProjectBusinessService projectBusinessService;

    @Test
    void should_create_project() {
        //given
        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("testName")
                .description("testDescription")
                .build();
        ProjectDocument projectDocument = ProjectDocument.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id("1")
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .build();
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(projectService.isProjectExist(projectRequest.getName(), userId)).thenReturn(false);
        when(projectMapper.toProjectDocument(projectRequest, userId)).thenReturn(projectDocument);
        when(projectService.createOrUpdate(projectDocument)).thenReturn(projectDocument);
        when(projectMapper.toProjectResponse(projectDocument)).thenReturn(projectResponse);

        //when
        ProjectResponse response = projectBusinessService.createProject(projectRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response).isSameAs(projectResponse);

        InOrder inOrder = inOrder(securityContextService, projectService, projectMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(projectService).isProjectExist(projectRequest.getName(), userId);
        inOrder.verify(projectMapper).toProjectDocument(projectRequest, userId);
        inOrder.verify(projectService).createOrUpdate(projectDocument);
        inOrder.verify(projectMapper).toProjectResponse(projectDocument);
    }

    @Test
    void should_throw_api_exception_when_project_already_exist() {
        //given
        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("testName")
                .description("testDescription")
                .build();
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(projectService.isProjectExist(projectRequest.getName(), userId)).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> projectBusinessService.createProject(projectRequest))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.PROJECT_ALREADY_EXIST.getMessageCode());

        InOrder inOrder = inOrder(securityContextService, projectService);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(projectService).isProjectExist(projectRequest.getName(), userId);
    }

    @Test
    void should_get_project_by_id() {
        //given
        String projectId = "testProjectId";
        ProjectDocument projectDocument = ProjectDocument.builder()
                .id(projectId)
                .name("testName")
                .description("testDescription")
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(projectId)
                .name(projectDocument.getName())
                .description(projectDocument.getDescription())
                .build();
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(projectService.getProjectByIdAndUserId(projectId, userId)).thenReturn(projectDocument);
        when(projectMapper.toProjectResponse(projectDocument)).thenReturn(projectResponse);

        //when
        ProjectResponse response = projectBusinessService.getProjectById(projectId);

        //then
        assertThat(response).isNotNull();
        assertThat(response).isSameAs(projectResponse);

        InOrder inOrder = inOrder(securityContextService, projectService, projectMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(projectService).getProjectByIdAndUserId(projectId, userId);
        inOrder.verify(projectMapper).toProjectResponse(projectDocument);
    }

    @Test
    void should_get_all_projects() {
        //given
        ProjectDocument projectDocument = ProjectDocument.builder()
                .id("1")
                .name("testName")
                .description("testDescription")
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(projectDocument.getId())
                .name(projectDocument.getName())
                .description(projectDocument.getDescription())
                .build();
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(projectService.getProjectsByUserId(userId)).thenReturn(List.of(projectDocument));
        when(projectMapper.toProjectResponse(projectDocument)).thenReturn(projectResponse);

        //when
        ProjectWrapperResponse response = projectBusinessService.getAllProjects();

        //then
        assertThat(response).isNotNull();
        assertThat(response.getProjects())
                .hasSize(1)
                .contains(projectResponse);
        assertThat(response.getSize()).isEqualTo(1);

        InOrder inOrder = inOrder(securityContextService, projectService, projectMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(projectService).getProjectsByUserId(userId);
        inOrder.verify(projectMapper).toProjectResponse(projectDocument);
    }

    @Test
    void should_update_project() {
        //given
        String projectId = "testProjectId";
        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("testName")
                .description("testDescription")
                .build();
        ProjectDocument projectDocument = ProjectDocument.builder()
                .id(projectId)
                .name("oldName")
                .description("oldDescription")
                .build();
        ProjectDocument updatedProjectDocument = ProjectDocument.builder()
                .id(projectId)
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(projectId)
                .name(updatedProjectDocument.getName())
                .description(updatedProjectDocument.getDescription())
                .build();
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(projectService.getProjectByIdAndUserId(projectId, userId)).thenReturn(projectDocument);
        when(projectService.createOrUpdate(any(ProjectDocument.class))).thenReturn(updatedProjectDocument);
        when(projectMapper.toProjectResponse(updatedProjectDocument)).thenReturn(projectResponse);

        //when
        ProjectResponse response = projectBusinessService.updateProject(projectId, projectRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response).isSameAs(projectResponse);

        InOrder inOrder = inOrder(securityContextService, projectService, projectMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(projectService).getProjectByIdAndUserId(projectId, userId);
        inOrder.verify(projectService).createOrUpdate(any(ProjectDocument.class));
        inOrder.verify(projectMapper).toProjectResponse(updatedProjectDocument);
    }

    @Test
    void should_delete_project_by_id() {
        //given
        String projectId = "testProjectId";
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);

        //when
        projectBusinessService.deleteProjectById(projectId);

        //then
        InOrder inOrder = inOrder(securityContextService, projectService, todoService);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(projectService).deleteProjectByIdAndUserId(projectId, userId);
        inOrder.verify(todoService).deleteTodosByProjectId(projectId);
    }
}