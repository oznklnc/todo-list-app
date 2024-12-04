package com.demo.todo.list.app.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponse extends BaseResponse {

    private String title;
    private String description;
    private String parentId;
    private Boolean completed;
    private Integer level;
    private String treeId;
    private int left;
    private int right;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default
    private List<TodoResponse> subTodos = new ArrayList<>();
}
