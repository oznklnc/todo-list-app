package com.demo.todo.list.app.service;

import com.demo.todo.list.app.entity.ProjectDocument;

import java.util.List;

public interface ProjectService {

    ProjectDocument createOrUpdate(ProjectDocument projectDocument);

    ProjectDocument getProjectByIdAndUserId(String id, String userId);

    List<ProjectDocument> getProjectsByUserId(String userId);

    void deleteProjectByIdAndUserId(String projectId, String loggedInUserId);

    boolean isProjectExist(String projectName, String userId);
}
