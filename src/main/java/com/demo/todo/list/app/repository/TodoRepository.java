package com.demo.todo.list.app.repository;

import com.demo.todo.list.app.entity.TodoDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Collection(TodoDocument.TODO_COLLECTION_NAME)
public interface TodoRepository extends CouchbaseRepository<TodoDocument, String> {

    Optional<TodoDocument> findByIdAndUserIdAndCompleted(String id, String userId, boolean completed);

    List<TodoDocument> findByProjectIdAndUserIdAndCompleted(String projectId, String userId, boolean completed);

    @Query("#{#n1ql.selectEntity} WHERE `right` >= $1 AND `treeId` = $2 AND `completed` = $3" )
    List<TodoDocument> findByRightGreaterThanEqual(int right, String treeId, boolean completed);

    void deleteAllByTreeIdAndLeftGreaterThanEqualAndRightLessThanEqual(String treeId, int left, int right);

    void deleteAllByProjectId(String projectId);

    @Query("UPDATE #{#n1ql.bucket} SET completed = true WHERE  `treeId` = $1 AND `left` >= $2 AND `right` <= $3")
    void updateByTreedIdAndLeftAndRightAsCompleted(String treeId, int left, int right);
}
