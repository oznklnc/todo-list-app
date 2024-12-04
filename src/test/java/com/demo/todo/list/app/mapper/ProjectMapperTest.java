package com.demo.todo.list.app.mapper;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.ProjectDocument;
import com.demo.todo.list.app.model.request.ProjectRequest;
import com.demo.todo.list.app.model.response.ProjectResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectMapperTest extends UnitTest {

    @InjectMocks
    private final ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    @Test
    void should_to_project_document() {
        //given
        String userId = "test";
        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("testName")
                .description("testDescription")
                .build();

        //when
        ProjectDocument projectDocument = projectMapper.toProjectDocument(projectRequest, userId);

        //then
        assertNotNull(projectDocument);
        assertEquals(projectDocument.getName(), projectRequest.getName());
        assertEquals(projectDocument.getDescription(), projectRequest.getDescription());
    }

    @Test
    void should_to_project_response() {
        //given
        ProjectDocument projectDocument = ProjectDocument.builder()
                .name("testName")
                .description("testDescription")
                .build();

        //when
        ProjectResponse projectResponse = projectMapper.toProjectResponse(projectDocument);

        //then
        assertNotNull(projectResponse);
        assertEquals(projectResponse.getName(), projectDocument.getName());
        assertEquals(projectResponse.getDescription(), projectDocument.getDescription());
    }
}