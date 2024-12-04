package com.demo.todo.list.app.service.business;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ProjectBusinessService extends BaseBusinessService {

    private final ProjectService projectService;
    private final TodoService todoService;
    private final ProjectMapper projectMapper;

    public ProjectBusinessService(SecurityContextService securityContextService,
                                  ProjectService projectService,
                                  TodoService todoService,
                                  ProjectMapper projectMapper) {
        super(securityContextService);
        this.projectService = projectService;
        this.todoService = todoService;
        this.projectMapper = projectMapper;
    }

    public ProjectResponse createProject(ProjectRequest projectRequest) {
        if (projectService.isProjectExist(projectRequest.getName(), getLoggedInUserId())) {
            throw new ApiException(ErrorCode.PROJECT_ALREADY_EXIST);
        }
        ProjectDocument projectDocument = projectMapper.toProjectDocument(projectRequest, getLoggedInUserId());
        return projectMapper.toProjectResponse(
                projectService.createOrUpdate(projectDocument)
        );
    }

    public ProjectResponse getProjectById(String id) {
        return projectMapper.toProjectResponse(
                projectService.getProjectByIdAndUserId(id, getLoggedInUserId())
        );
    }

    public ProjectWrapperResponse getAllProjects() {
        List<ProjectResponse> projectResponses = projectService.getProjectsByUserId(getLoggedInUserId())
                .stream()
                .map(projectMapper::toProjectResponse)
                .toList();
        return ProjectWrapperResponse.builder()
                .projects(projectResponses)
                .size(projectResponses.size())
                .build();
    }

    public ProjectResponse updateProject(String projectId,ProjectRequest projectRequest) {
        ProjectDocument projectDocument = projectService.getProjectByIdAndUserId(projectId, getLoggedInUserId());
        projectDocument.setName(projectRequest.getName());
        projectDocument.setDescription(projectRequest.getDescription());
        return projectMapper.toProjectResponse(
                projectService.createOrUpdate(projectDocument)
        );
    }

    @Transactional
    public void deleteProjectById(String projectId) {
        projectService.deleteProjectByIdAndUserId(projectId, getLoggedInUserId());
        todoService.deleteTodosByProjectId(projectId);
    }
}
