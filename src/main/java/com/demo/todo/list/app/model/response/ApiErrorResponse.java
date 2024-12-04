package com.demo.todo.list.app.model.response;

import com.demo.todo.list.app.constant.TodoListConstants;
import com.demo.todo.list.app.model.dto.ApiErrorDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TodoListConstants.ISO_8601_PATTERN)
    private Date createdDate;
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiErrorDto> messages;

}
