package com.demo.todo.list.app.service.business;

import com.demo.todo.list.app.entity.TodoDocument;
import com.demo.todo.list.app.generator.TreeIdGenerator;
import com.demo.todo.list.app.mapper.TodoMapper;
import com.demo.todo.list.app.model.request.TodoRequest;
import com.demo.todo.list.app.model.response.TodoResponse;
import com.demo.todo.list.app.model.response.TodoWrapperResponse;
import com.demo.todo.list.app.service.SecurityContextService;
import com.demo.todo.list.app.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TodoBusinessService extends BaseBusinessService {

    private final TodoService todoService;
    private final TodoMapper todoMapper;
    private final TreeIdGenerator treeIdGenerator;


    public TodoBusinessService(SecurityContextService securityContextService,
                               TodoService todoService,
                               TodoMapper todoMapper,
                               TreeIdGenerator treeIdGenerator) {
        super(securityContextService);
        this.todoService = todoService;
        this.todoMapper = todoMapper;
        this.treeIdGenerator = treeIdGenerator;
    }

    public TodoResponse addTodo(TodoRequest todoRequest) {
        TodoDocument todoDocument = todoMapper.toTodoDocument(todoRequest, getLoggedInUserId());
        todoDocument.setTreeId(treeIdGenerator.generateTreeId());
        return todoMapper.toTodoResponse(
                todoService.create(todoDocument)
        );
    }


    @Transactional
    public TodoResponse addSubTodo(TodoRequest todoRequest, String parentId) {
        TodoDocument parent = todoService.getTodoByIdAndUserIdNotCompleted(parentId, getLoggedInUserId());
        todoService.updateRightValues(parent.getRight(), parent.getTreeId());
        TodoDocument todoDocument = todoMapper.toTodoDocument(todoRequest, parent);
        todoDocument.setId(null);
        todoDocument.setVersion(0);
        return todoMapper.toTodoResponse(
                todoService.create(todoDocument)
        );
    }

    public TodoResponse getTodoById(String id) {
        return todoMapper.toTodoResponse(
                todoService.getTodoByIdAndUserIdNotCompleted(id, getLoggedInUserId())
        );
    }

    public TodoWrapperResponse getTodos(String projectId) {
        List<TodoDocument> todoDocuments = todoService.getTodos(projectId, getLoggedInUserId(), false);

        return todoMapper.toTodoWrapperResponse(todoDocuments);
    }

    public void deleteTodoById(String id) {
        TodoDocument todoDocument = todoService.getTodoByIdAndUserIdNotCompleted(id, getLoggedInUserId());
        todoService.deleteByTreeIdAndLeftAndRight(todoDocument.getTreeId(), todoDocument.getLeft(), todoDocument.getRight());
    }

    public void updateAsCompleted(String id) {
        TodoDocument todoDocument = todoService.getTodoByIdAndUserIdNotCompleted(id, getLoggedInUserId());
        todoService.updateByTreedIdAndLeftAndRightAsCompleted(todoDocument.getTreeId(), todoDocument.getLeft(), todoDocument.getRight());
    }

    public TodoResponse updateTodoById(String id, TodoRequest todoRequest) {
        TodoDocument todoDocument = todoService.getTodoByIdAndUserIdNotCompleted(id, getLoggedInUserId());
        todoDocument.setTitle(todoRequest.getTitle());
        todoDocument.setDescription(todoRequest.getDescription());
        return todoMapper.toTodoResponse(
                todoService.create(todoDocument)
        );
    }
}
