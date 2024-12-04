package com.demo.todo.list.app.repository;

import com.demo.todo.list.app.entity.ProjectDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Collection(ProjectDocument.PROJECT_COLLECTION_NAME)
public interface ProjectRepository extends CouchbaseRepository<ProjectDocument, String> {

    boolean existsProjectDocumentByNameAndUserId(String name, String userId);

    Optional<ProjectDocument> findByIdAndUserId(String id, String userId);

    List<ProjectDocument> findByUserId(String userId);

    void deleteProjectDocumentByIdAndUserId(String id, String userId);
}
