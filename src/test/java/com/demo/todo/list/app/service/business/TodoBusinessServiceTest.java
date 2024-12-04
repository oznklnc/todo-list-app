package com.demo.todo.list.app.service.business;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.TodoDocument;
import com.demo.todo.list.app.generator.TreeIdGenerator;
import com.demo.todo.list.app.mapper.TodoMapper;
import com.demo.todo.list.app.model.request.TodoRequest;
import com.demo.todo.list.app.model.response.TodoResponse;
import com.demo.todo.list.app.model.response.TodoWrapperResponse;
import com.demo.todo.list.app.service.SecurityContextService;
import com.demo.todo.list.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class TodoBusinessServiceTest extends UnitTest {

    @Mock
    SecurityContextService securityContextService;
    @Mock
    TodoService todoService;
    @Mock
    TodoMapper todoMapper;
    @Mock
    TreeIdGenerator treeIdGenerator;
    @InjectMocks
    TodoBusinessService todoBusinessService;
    @Captor
    ArgumentCaptor<TodoDocument> todoDocumentArgumentCaptor;

    @Test
    void should_add_todo() {
        //given
        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();
        TodoDocument todoDocument = TodoDocument.builder()
                .title(todoRequest.getTitle())
                .description(todoRequest.getDescription())
                .projectId(todoRequest.getProjectId())
                .build();
        TodoResponse todoResponse = TodoResponse.builder()
                .id("1")
                .title(todoRequest.getTitle())
                .description(todoRequest.getDescription())
                .build();
        String userId = "testUserId";
        String testTreeId = "testTreeId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(todoMapper.toTodoDocument(todoRequest, userId)).thenReturn(todoDocument);
        when(treeIdGenerator.generateTreeId()).thenReturn(testTreeId);
        when(todoService.create(any())).thenReturn(todoDocument);
        when(todoMapper.toTodoResponse(todoDocument)).thenReturn(todoResponse);

        //when
        TodoResponse response = todoBusinessService.addTodo(todoRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("1");
        assertThat(response.getTitle()).isEqualTo(todoRequest.getTitle());
        assertThat(response.getDescription()).isEqualTo(todoRequest.getDescription());

        InOrder inOrder = inOrder(securityContextService, todoMapper, treeIdGenerator, todoService);
        inOrder.verify(todoMapper).toTodoDocument(todoRequest, userId);
        inOrder.verify(treeIdGenerator).generateTreeId();
        inOrder.verify(todoService).create(todoDocumentArgumentCaptor.capture());
        inOrder.verify(todoMapper).toTodoResponse(todoDocument);

        TodoDocument capturedTodoDocument = todoDocumentArgumentCaptor.getValue();
        assertThat(capturedTodoDocument).isNotNull();
        assertThat(capturedTodoDocument.getTreeId()).isEqualTo(testTreeId);
    }

    @Test
    void should_add_sub_todo() {
        //given
        String parentId = "1";
        String userId = "testUserId";
        int left = 1;
        int right = 2;
        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();
        TodoDocument parent = TodoDocument.builder()
                .treeId("testTreeId")
                .level(0)
                .left(left)
                .right(right)
                .build();
        TodoDocument childTodo = TodoDocument.builder()
                .parentId(parentId)
                .left(parent.getRight())
                .right(parent.getRight() + 1)
                .treeId(parent.getTreeId())
                .build();
        TodoResponse todoResponse = TodoResponse.builder().build();

        when(securityContextService.getUserId()).thenReturn(userId);
        when(todoService.getTodoByIdAndUserIdNotCompleted(parentId, userId)).thenReturn(parent);
        when(todoMapper.toTodoDocument(todoRequest, parent)).thenReturn(childTodo);
        when(todoService.create(any(TodoDocument.class))).thenReturn(childTodo);
        when(todoMapper.toTodoResponse(childTodo)).thenReturn(todoResponse);

        //when
        TodoResponse result = todoBusinessService.addSubTodo(todoRequest, parentId);

        //then
        assertThat(result).isNotNull().isSameAs(todoResponse);
        InOrder inOrder = inOrder(securityContextService, todoService, todoMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(todoService).getTodoByIdAndUserIdNotCompleted(parentId, userId);
        inOrder.verify(todoService).updateRightValues(parent.getRight(), parent.getTreeId());
        inOrder.verify(todoService, times(1)).create(todoDocumentArgumentCaptor.capture());

        List<TodoDocument> allValues = todoDocumentArgumentCaptor.getAllValues();
        assertThat(allValues).isNotNull().hasSize(1);
        assertThat(allValues.get(0).getParentId()).isEqualTo(parentId);
        assertThat(allValues.get(0).getLeft()).isEqualTo(parent.getRight());
        assertThat(allValues.get(0).getRight()).isEqualTo(parent.getRight() + 1);

    }

    @Test
    void should_get_todo_by_id() {
        //given
        TodoDocument todoDocument = TodoDocument.builder()
                .build();
        TodoResponse todoResponse = TodoResponse.builder().build();

        String todoId = "1";
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(todoService.getTodoByIdAndUserIdNotCompleted(todoId, userId)).thenReturn(todoDocument);
        when(todoMapper.toTodoResponse(todoDocument)).thenReturn(todoResponse);

        //when
        TodoResponse result = todoBusinessService.getTodoById(todoId);

        //then
        assertThat(result).isNotNull().isSameAs(todoResponse);
        InOrder inOrder = inOrder(securityContextService, todoService, todoMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(todoService).getTodoByIdAndUserIdNotCompleted(todoId, userId);
        inOrder.verify(todoMapper).toTodoResponse(todoDocument);
    }

    @Test
    void should_get_todos_by_project_id() {
        //given
        TodoDocument todoDocument = TodoDocument.builder()
                .build();
        TodoResponse todoResponse = TodoResponse.builder().build();
        TodoWrapperResponse todoWrapperResponse = TodoWrapperResponse.builder()
                .total(1)
                .todoResponses(List.of(todoResponse))
                .build();

        String projectId = "1";
        String userId = "testUserId";

        when(securityContextService.getUserId()).thenReturn(userId);
        when(todoService.getTodos(projectId, userId, false)).thenReturn(List.of(todoDocument));
        when(todoMapper.toTodoWrapperResponse(List.of(todoDocument))).thenReturn(todoWrapperResponse);

        //when
        TodoWrapperResponse result = todoBusinessService.getTodos(projectId);

        //then
        assertThat(result).isNotNull().isSameAs(todoWrapperResponse);
        InOrder inOrder = inOrder(securityContextService, todoService, todoMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(todoService).getTodos(projectId, userId, false);
        inOrder.verify(todoMapper).toTodoWrapperResponse(List.of(todoDocument));
    }

    @Test
    void should_delete_todo_by_id() {
        //given
        String todoId = "1";
        String userId = "testUserId";
        TodoDocument todoDocument = TodoDocument.builder()
                .id(todoId)
                .left(1)
                .right(2)
                .build();

        when(securityContextService.getUserId()).thenReturn(userId);
        when(todoService.getTodoByIdAndUserIdNotCompleted(todoId, userId)).thenReturn(todoDocument);

        //when
        todoBusinessService.deleteTodoById(todoId);

        //then
        InOrder inOrder = inOrder(securityContextService, todoService);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(todoService).getTodoByIdAndUserIdNotCompleted(todoId, userId);
        inOrder.verify(todoService).deleteByTreeIdAndLeftAndRight(todoDocument.getTreeId(), todoDocument.getLeft(), todoDocument.getRight());
    }

    @Test
    void should_update_as_completed() {
        //given
        String todoId = "1";
        String userId = "testUserId";
        TodoDocument todoDocument = TodoDocument.builder()
                .id(todoId)
                .treeId("testTreeId")
                .left(1)
                .right(2)
                .build();

        when(securityContextService.getUserId()).thenReturn(userId);
        when(todoService.getTodoByIdAndUserIdNotCompleted(todoId, userId)).thenReturn(todoDocument);

        //when
        todoBusinessService.updateAsCompleted(todoId);

        //then
        InOrder inOrder = inOrder(securityContextService, todoService);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(todoService).getTodoByIdAndUserIdNotCompleted(todoId, userId);
        inOrder.verify(todoService).updateByTreedIdAndLeftAndRightAsCompleted(todoDocument.getTreeId(), todoDocument.getLeft(), todoDocument.getRight());
    }

    @Test
    void should_update_todo_by_id() {
        //given
        String todoId = "1";
        String userId = "testUserId";

        TodoRequest todoRequest = TodoRequest.builder()
                .title("testTitle")
                .description("testDescription")
                .projectId("testProjectId")
                .build();
        TodoDocument todoDocument = TodoDocument.builder()
                .id(todoId)
                .title(todoRequest.getTitle())
                .description(todoRequest.getDescription())
                .projectId(todoRequest.getProjectId())
                .build();
        TodoResponse todoResponse = TodoResponse.builder().build();

        when(securityContextService.getUserId()).thenReturn(userId);
        when(todoService.getTodoByIdAndUserIdNotCompleted(todoId, userId)).thenReturn(todoDocument);
        when(todoService.create(todoDocumentArgumentCaptor.capture())).thenReturn(todoDocument);
        when(todoMapper.toTodoResponse(todoDocument)).thenReturn(todoResponse);

        //when
        TodoResponse result = todoBusinessService.updateTodoById(todoId, todoRequest);

        //then
        assertThat(result).isNotNull().isSameAs(todoResponse);
        InOrder inOrder = inOrder(securityContextService, todoService, todoMapper);
        inOrder.verify(securityContextService).getUserId();
        inOrder.verify(todoService).getTodoByIdAndUserIdNotCompleted(todoId, userId);
        inOrder.verify(todoService).create(todoDocumentArgumentCaptor.capture());

        TodoDocument capturedValue = todoDocumentArgumentCaptor.getValue();
        assertThat(capturedValue).isNotNull();
        assertThat(capturedValue.getTitle()).isEqualTo(todoRequest.getTitle());
        assertThat(capturedValue.getDescription()).isEqualTo(todoRequest.getDescription());
    }
}