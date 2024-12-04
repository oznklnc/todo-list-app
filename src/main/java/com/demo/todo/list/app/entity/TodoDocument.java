package com.demo.todo.list.app.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TodoDocument {

    public final static String TODO_COLLECTION_NAME = "todo";

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String title;

    @Field
    private String description;

    @Field
    private boolean completed;

    @Field
    private String parentId;

    @Field
    private String projectId;

    @Field
    private String userId;

    @Field
    private int left;

    @Field
    private int right;

    @Field
    private int level;

    @Field
    private String treeId;

    @CreatedDate
    @Field
    private Date createdAt;


    @LastModifiedDate
    @Field
    private Date updatedAt;

    @Version
    private long version;

}
