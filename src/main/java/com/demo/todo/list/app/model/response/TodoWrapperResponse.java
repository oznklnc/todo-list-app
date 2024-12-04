package com.demo.todo.list.app.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TodoWrapperResponse {

    private Integer total;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TodoResponse> todoResponses;
}
