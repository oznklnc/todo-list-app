package com.demo.todo.list.app.mapper;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.TodoDocument;
import com.demo.todo.list.app.model.request.TodoRequest;
import com.demo.todo.list.app.model.response.TodoResponse;
import com.demo.todo.list.app.model.response.TodoWrapperResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoMapperTest extends UnitTest {

    @InjectMocks
    private final TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);

    @Test
    void should_to_todo_document_for_first_create_mapper() {
        //given
        String userId = "test";
        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();

        //when
        TodoDocument todoDocument = todoMapper.toTodoDocument(todoRequest, userId);

        //then
        assertThat(todoDocument).isNotNull();
        assertThat(todoDocument.getTitle()).isEqualTo(todoRequest.getTitle());
        assertThat(todoDocument.getDescription()).isEqualTo(todoRequest.getDescription());
        assertThat(todoDocument.getProjectId()).isEqualTo(todoRequest.getProjectId());
        assertThat(todoDocument.getLeft()).isEqualTo(1);
        assertThat(todoDocument.getRight()).isEqualTo(2);
        assertThat(todoDocument.getLevel()).isEqualTo(0);
    }

    @Test
    void should_map_to_todo_document_second_create_mapper() {
        //given
        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();
        TodoDocument todoDocument = TodoDocument.builder()
                .userId("userId")
                .treeId("treeId")
                .level(0)
                .right(2)
                .left(1)
                .build();

        //when
        TodoDocument response = todoMapper.toTodoDocument(todoRequest, todoDocument);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(todoRequest.getTitle());
        assertThat(response.getDescription()).isEqualTo(todoRequest.getDescription());
        assertThat(response.getProjectId()).isEqualTo(todoRequest.getProjectId());
        assertThat(response.getUserId()).isEqualTo(todoDocument.getUserId());
        assertThat(response.getTreeId()).isEqualTo(todoDocument.getTreeId());
        assertThat(response.getLeft()).isEqualTo(todoDocument.getRight());
        assertThat(response.getRight()).isEqualTo(todoDocument.getRight()+1);
    }

    @Test
    void should_map_todo_response() {
        //given
        TodoDocument todoDocument = TodoDocument.builder()
                .title("title")
                .description("description")
                .parentId("parentId")
                .completed(false)
                .level(0)
                .left(1)
                .right(2)
                .build();

        //when
        TodoResponse response = todoMapper.toTodoResponse(todoDocument);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(todoDocument.getTitle());
        assertThat(response.getDescription()).isEqualTo(todoDocument.getDescription());
        assertThat(response.getParentId()).isEqualTo(todoDocument.getParentId());
        assertThat(response.getCompleted()).isEqualTo(todoDocument.isCompleted());
        assertThat(response.getLeft()).isEqualTo(todoDocument.getLeft());
        assertThat(response.getRight()).isEqualTo(todoDocument.getRight());

    }

    @Test
    void should_map_todo_wrapper_response() {
        //given
        TodoDocument rootTodo1 = TodoDocument.builder()
                .id("1")
                .title("meyve al")
                .description("meyve almak için yapılmıştır")
                .completed(false)
                .parentId(null)
                .projectId("test1")
                .userId("userId")
                .level(0)
                .left(1)
                .right(5)
                .treeId("treeId1")
                .build();

        TodoDocument childTodo = TodoDocument.builder()
                .id("2")
                .title("elma al")
                .description("elma almak için yapılmıştır")
                .completed(false)
                .parentId(rootTodo1.getId())
                .projectId("test1")
                .userId("userId")
                .level(1)
                .left(2)
                .right(3)
                .treeId("treeId1")
                .build();

        TodoDocument rootTodo2 = TodoDocument.builder()
                .id("3")
                .title("sebze al")
                .description("sebze almak için yapılmıştır")
                .completed(false)
                .parentId(null)
                .projectId("test1")
                .userId("userId")
                .level(0)
                .left(1)
                .right(2)
                .treeId("treeId2")
                .build();

        //when
        TodoWrapperResponse response = todoMapper.toTodoWrapperResponse(List.of(rootTodo1, childTodo, rootTodo2));

        //then
        assertThat(response).isNotNull();
        assertThat(response.getTotal()).isEqualTo(3);
        assertThat(response.getTodoResponses()).isNotNull();
        assertThat(response.getTodoResponses()).hasSize(2);
        assertThat(response.getTodoResponses().get(0).getTitle()).isEqualTo(rootTodo1.getTitle());
        assertThat(response.getTodoResponses().get(0).getDescription()).isEqualTo(rootTodo1.getDescription());
        assertThat(response.getTodoResponses().get(0).getCompleted()).isEqualTo(rootTodo1.isCompleted());
        assertThat(response.getTodoResponses().get(0).getSubTodos()).isNotEmpty().hasSize(1);
        assertThat(response.getTodoResponses().get(1).getTitle()).isEqualTo(rootTodo2.getTitle());
        assertThat(response.getTodoResponses().get(1).getDescription()).isEqualTo(rootTodo2.getDescription());
        assertThat(response.getTodoResponses().get(1).getCompleted()).isEqualTo(rootTodo2.isCompleted());
        assertThat(response.getTodoResponses().get(1).getSubTodos()).isEmpty();



    }
}