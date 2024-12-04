package com.demo.todo.list.app.service.impl;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.ProjectDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectServiceImplTest extends UnitTest {

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectServiceImpl projectService;

    @Test
    void should_create_or_update_project() {
        //given
        ProjectDocument projectDocument = ProjectDocument.builder()
                .name("testName")
                .description("testDescription")
                .userId("testUserId")
                .build();

        when(projectRepository.save(projectDocument)).thenReturn(projectDocument);

        //when
        projectService.createOrUpdate(projectDocument);

        //then
        verify(projectRepository).save(projectDocument);
    }

    @Test
    void should_get_project_by_id_and_user_id() {
        //given
        String userId = "testUserId";
        String id = "testId";
        ProjectDocument projectDocument = ProjectDocument.builder()
                .name("testName")
                .description("testDescription")
                .userId(userId)
                .build();

        when(projectRepository.findByIdAndUserId(id, userId)).thenReturn(java.util.Optional.of(projectDocument));

        //when
        ProjectDocument response = projectService.getProjectByIdAndUserId(id, userId);

        //then
        assertThat(response).isNotNull();
        assertThat(response).isSameAs(projectDocument);
        verify(projectRepository).findByIdAndUserId(id, userId);
    }

    @Test
    void should_throw_exception_when_project_not_found() {
        //given
        String userId = "testUserId";
        String id = "testId";

        when(projectRepository.findByIdAndUserId(id, userId)).thenReturn(java.util.Optional.empty());

        //when
        assertThatThrownBy(() -> projectService.getProjectByIdAndUserId(id, userId))
            .isInstanceOf(ApiException.class)
            .hasMessage(ErrorCode.PROJECT_NOT_FOUND.getMessageCode());
    }

    @Test
    void should_get_projects_by_user_id() {
        //given
        String userId = "testUserId";
        ProjectDocument projectDocument = ProjectDocument.builder()
                .name("testName")
                .description("testDescription")
                .userId(userId)
                .build();
        when(projectRepository.findByUserId(userId)).thenReturn(List.of(projectDocument));

        //when
        List<ProjectDocument> result = projectService.getProjectsByUserId(userId);

        //then
        assertThat(result).isNotEmpty();
        assertThat(result).contains(projectDocument);
        verify(projectRepository).findByUserId(userId);
    }

    @Test
    void should_delete_project_by_id_and_user_id() {
        //given
        String projectId = "testProjectId";
        String loggedInUserId = "testUserId";

        //when
        projectService.deleteProjectByIdAndUserId(projectId, loggedInUserId);

        //then
        verify(projectRepository).deleteProjectDocumentByIdAndUserId(projectId, loggedInUserId);
    }

    @Test
    void should_check_project_exist() {
        //given
        String projectName = "testProjectName";
        String userId = "testUserId";
        when(projectRepository.existsProjectDocumentByNameAndUserId(projectName, userId)).thenReturn(true);

        //when
        boolean result = projectService.isProjectExist(projectName, userId);

        //then
        assertThat(result).isTrue();
        verify(projectRepository).existsProjectDocumentByNameAndUserId(projectName, userId);
    }
}