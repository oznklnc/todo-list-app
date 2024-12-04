package com.demo.todo.list.app.service.impl;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.TodoDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class TodoServiceImplTest extends UnitTest {

    @Mock
    TodoRepository todoRepository;
    @InjectMocks
    TodoServiceImpl todoService;

    @Captor
    ArgumentCaptor<List<TodoDocument>> todoDocumentListArgumentCaptor;

    @Test
    void should_get_todos() {
        //given
        String projectId = "projectId";
        String userId = "userId";
        boolean completed = false;

        TodoDocument todoDocument = TodoDocument.builder()
                .projectId(projectId)
                .userId(userId)
                .completed(completed)
                .build();

        when(todoRepository.findByProjectIdAndUserIdAndCompleted(projectId, userId, completed)).thenReturn(List.of(todoDocument));

        //when
        List<TodoDocument> todos = todoService.getTodos(projectId, userId, completed);

        //then
        verify(todoRepository).findByProjectIdAndUserIdAndCompleted(projectId, userId, completed);
        assertThat(todos).isNotNull();
        assertThat(todos).hasSize(1);
        assertThat(todos.get(0)).isEqualTo(todoDocument);

    }

    @Test
    void should_get_todo_by_id_and_user_id_not_completed() {
        //given
        String id = "id";
        String userId = "userId";
        TodoDocument todoDocument = TodoDocument.builder()
                .id(id)
                .userId(userId)
                .completed(false)
                .build();

        when(todoRepository.findByIdAndUserIdAndCompleted(id, userId, false)).thenReturn(java.util.Optional.of(todoDocument));

        //when
        TodoDocument todo = todoService.getTodoByIdAndUserIdNotCompleted(id, userId);

        //then
        verify(todoRepository).findByIdAndUserIdAndCompleted(id, userId, false);
        assertThat(todo).isNotNull();
        assertThat(todo).isSameAs(todoDocument);
    }

    @Test
    void should_throw_api_exception_when_todo_not_found() {
        //given
        String id = "id";
        String userId = "userId";

        when(todoRepository.findByIdAndUserIdAndCompleted(id, userId, false)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> todoService.getTodoByIdAndUserIdNotCompleted(id, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.TODO_NOT_FOUND.getMessageCode());
    }

    @Test
    void should_create() {
        //given
        TodoDocument todoDocument = TodoDocument.builder()
                .title("title")
                .description("description")
                .build();

        when(todoRepository.save(todoDocument)).thenReturn(todoDocument);

        //when
        TodoDocument result = todoService.create(todoDocument);

        //then
        verify(todoRepository).save(todoDocument);
        assertThat(result).isNotNull();
        assertThat(result).isSameAs(todoDocument);
    }


    @Test
    void should_find_by_right_greater_than_equal() {
        //given
        int right = 1;
        String treeId = "treeId";
        boolean completed = false;

        TodoDocument todoDocument = TodoDocument.builder()
                .right(right)
                .treeId(treeId)
                .completed(completed)
                .build();

        when(todoRepository.findByRightGreaterThanEqual(right, treeId, completed)).thenReturn(List.of(todoDocument));

        //when
        List<TodoDocument> todos = todoService.findByRightGreaterThanEqual(right, treeId);

        //then
        verify(todoRepository).findByRightGreaterThanEqual(right, treeId, completed);
        assertThat(todos)
                .isNotNull()
                .hasSize(1)
                .contains(todoDocument);
    }

    @Test
    void should_delete_by_tree_id_and_left_and_right() {
        //given
        String treeId = "treeId";
        int left = 1;
        int right = 2;

        //when
        todoService.deleteByTreeIdAndLeftAndRight(treeId, left, right);

        //then
        verify(todoRepository).deleteAllByTreeIdAndLeftGreaterThanEqualAndRightLessThanEqual(treeId, left, right);
    }

    @Test
    void should_delete_todos_by_project_id() {
        //given
        String projectId = "projectId";

        //when
        todoService.deleteTodosByProjectId(projectId);

        //then
        verify(todoRepository).deleteAllByProjectId(projectId);
    }

    @Test
    void should_update_by_treed_id_and_left_and_right_as_completed() {
        //given
        String treeId = "treeId";
        int left = 1;
        int right = 2;

        //when
        todoService.updateByTreedIdAndLeftAndRightAsCompleted(treeId, left, right);

        //then
        verify(todoRepository).updateByTreedIdAndLeftAndRightAsCompleted(treeId, left, right);
    }

    @Test
    void should_update_right_values() {
        //given
        int left = 1;
        int right = 2;
        String treeId = "treeId";
        boolean completed = false;

        TodoDocument todoDocument = TodoDocument.builder()
                .left(left)
                .right(right)
                .treeId(treeId)
                .completed(completed)
                .build();

        when(todoRepository.findByRightGreaterThanEqual(right, treeId, completed)).thenReturn(List.of(todoDocument));

        //when
        todoService.updateRightValues(right, treeId);

        //then
        InOrder inOrder = inOrder(todoRepository);
        inOrder.verify(todoRepository).findByRightGreaterThanEqual(right, treeId, completed);
        inOrder.verify(todoRepository).saveAll(todoDocumentListArgumentCaptor.capture());

        List<TodoDocument> capturedTodoDocuments = todoDocumentListArgumentCaptor.getValue();
        assertThat(capturedTodoDocuments)
                .isNotNull()
                .hasSize(1);
        assertThat(capturedTodoDocuments.get(0).getLeft()).isEqualTo(left);
        assertThat(capturedTodoDocuments.get(0).getRight()).isEqualTo(right + 2);
    }
}