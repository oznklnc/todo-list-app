package com.demo.todo.list.app.service;

import com.demo.todo.list.app.entity.TodoDocument;

import java.util.List;

public interface TodoService {

    List<TodoDocument> getTodos(String projectId, String userId, boolean completed);

    TodoDocument getTodoByIdAndUserIdNotCompleted(String id, String userId);

    TodoDocument create(TodoDocument todoDocument);

    List<TodoDocument> findByRightGreaterThanEqual(int right, String treeId);

    void deleteByTreeIdAndLeftAndRight(String treeId, int left, int right);

    void  deleteTodosByProjectId(String projectId);

    void updateByTreedIdAndLeftAndRightAsCompleted(String treeId, int left, int right);

    void updateRightValues(int right, String treeId);
}
