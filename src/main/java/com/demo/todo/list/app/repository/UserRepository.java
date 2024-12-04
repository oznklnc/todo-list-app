package com.demo.todo.list.app.repository;

import com.demo.todo.list.app.entity.UserDocument;
import org.springframework.data.couchbase.repository.Collection;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Collection(UserDocument.USER_COLLECTION_NAME)
public interface UserRepository extends CouchbaseRepository<UserDocument, String> {

    Optional<UserDocument> findByEmail(String email);

    boolean existsUserDocumentsByEmail(String email);
}
