package com.demo.todo.list.app.mapper;

import com.demo.todo.list.app.entity.ProjectDocument;
import com.demo.todo.list.app.model.request.ProjectRequest;
import com.demo.todo.list.app.model.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface ProjectMapper {

    ProjectDocument toProjectDocument(ProjectRequest request, String userId);

    ProjectResponse toProjectResponse(ProjectDocument document);
}
