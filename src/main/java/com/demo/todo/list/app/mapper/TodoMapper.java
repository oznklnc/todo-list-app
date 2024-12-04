package com.demo.todo.list.app.mapper;

import com.demo.todo.list.app.entity.TodoDocument;
import com.demo.todo.list.app.model.request.TodoRequest;
import com.demo.todo.list.app.model.response.TodoResponse;
import com.demo.todo.list.app.model.response.TodoWrapperResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface TodoMapper {

    @Mapping(target = "left", constant = "1")
    @Mapping(target = "right", constant = "2")
    @Mapping(target = "level", constant = "0")
    TodoDocument toTodoDocument(TodoRequest request, String userId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "request.title")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "projectId", source = "request.projectId")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "userId", source = "parent.userId")
    @Mapping(target = "treeId", source = "parent.treeId")
    @Mapping(target = "left", expression = "java(parent.getRight())")
    @Mapping(target = "right", expression = "java(parent.getRight() + 1)")
    @Mapping(target = "level", expression = "java(parent.getLevel() + 1)")
    TodoDocument toTodoDocument(TodoRequest request, TodoDocument parent);

    TodoResponse toTodoResponse(TodoDocument document);

    default TodoWrapperResponse toTodoWrapperResponse(List<TodoDocument> todoDocuments) {
        Map<String, List<TodoDocument>> groupedByParentId = todoDocuments.stream()
                .filter(todo -> todo.getParentId() != null)
                .collect(Collectors.groupingBy(TodoDocument::getParentId));

        Map<String, TodoResponse> responseMap = todoDocuments.stream()
                .collect(Collectors.toMap(TodoDocument::getId, this::toTodoResponse));


        Queue<TodoResponse> queue = new LinkedList<>();
        responseMap.forEach((k, v) -> {
            if (v.getParentId() == null) {
                queue.add(v);
            }

        });
        responseMap.values().forEach(todo -> {
            if (todo.getParentId() == null) {
                queue.add(todo);
            }
        });


        while (!queue.isEmpty()) {
            TodoResponse current = queue.poll();
            List<TodoResponse> subTodos = groupedByParentId.getOrDefault(current.getId(), List.of()).stream()
                    .map(subTodo -> responseMap.get(subTodo.getId()))
                    .collect(Collectors.toList());


            current.setSubTodos(subTodos);
            queue.addAll(subTodos);
        }

        List<TodoResponse> allTodos = responseMap.values().stream()
                .filter(todo -> todo.getParentId() == null)
                .collect(Collectors.toList());

        return TodoWrapperResponse.builder()
                .total(todoDocuments.size())
                .todoResponses(allTodos)
                .build();
    }
}
