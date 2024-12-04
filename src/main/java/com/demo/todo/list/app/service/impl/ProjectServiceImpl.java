package com.demo.todo.list.app.service.impl;

import com.demo.todo.list.app.entity.ProjectDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.repository.ProjectRepository;
import com.demo.todo.list.app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectDocument createOrUpdate(ProjectDocument projectDocument) {
        return projectRepository.save(projectDocument);
    }

    @Override
    public ProjectDocument getProjectByIdAndUserId(String id, String userId) {
        return projectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.PROJECT_NOT_FOUND));
    }

    @Override
    public List<ProjectDocument> getProjectsByUserId(String userId) {
        return projectRepository.findByUserId(userId);
    }

    @Override
    public void deleteProjectByIdAndUserId(String projectId, String loggedInUserId) {
        projectRepository.deleteProjectDocumentByIdAndUserId(projectId, loggedInUserId);
    }

    @Override
    public boolean isProjectExist(String projectName, String userId) {
        return projectRepository.existsProjectDocumentByNameAndUserId(projectName, userId);
    }
}
