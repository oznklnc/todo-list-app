package com.demo.todo.list.app.service.impl;

import com.demo.todo.list.app.entity.TodoDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.repository.TodoRepository;
import com.demo.todo.list.app.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public List<TodoDocument> getTodos(String projectId, String userId, boolean completed) {
        return todoRepository.findByProjectIdAndUserIdAndCompleted(projectId, userId, completed);
    }

    @Override
    public TodoDocument getTodoByIdAndUserIdNotCompleted(String id, String userId) {
        return todoRepository.findByIdAndUserIdAndCompleted(id, userId, false)
                .orElseThrow(()-> new ApiException(ErrorCode.TODO_NOT_FOUND));
    }


    @Override
    public TodoDocument create(TodoDocument todoDocument) {
        return todoRepository.save(todoDocument);
    }

    @Override
    public List<TodoDocument> findByRightGreaterThanEqual(int right, String treeId) {
        return todoRepository.findByRightGreaterThanEqual(right, treeId, false);
    }

    @Override
    public void deleteByTreeIdAndLeftAndRight(String treeId, int left, int right) {
        todoRepository.deleteAllByTreeIdAndLeftGreaterThanEqualAndRightLessThanEqual(treeId, left, right);
    }

    @Override
    public void deleteTodosByProjectId(String projectId) {
        todoRepository.deleteAllByProjectId(projectId);
    }

    @Override
    public void updateByTreedIdAndLeftAndRightAsCompleted(String treeId, int left, int right) {
        todoRepository.updateByTreedIdAndLeftAndRightAsCompleted(treeId, left, right);
    }

    @Override
    public void updateRightValues(int right, String treeId) {
        List<TodoDocument> affectedNodes = todoRepository.findByRightGreaterThanEqual(right, treeId, false);
        List<TodoDocument> updatedNodes = affectedNodes.stream()
                .peek(node -> {
                    node.setRight(node.getRight() + 2);
                    if (node.getLeft() > right) {
                        node.setLeft(node.getLeft() + 2);
                    }
                })
                .collect(Collectors.toList());
        todoRepository.saveAll(updatedNodes);

    }


}
