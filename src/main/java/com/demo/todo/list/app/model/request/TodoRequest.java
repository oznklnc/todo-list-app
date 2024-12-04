package com.demo.todo.list.app.model.request;

import com.demo.todo.list.app.constant.ExceptionConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TodoRequest {

    @NotBlank(message = ExceptionConstants.TODO_TITLE_NOT_BLANK)
    private String title;
    private String description;
    @NotBlank(message = ExceptionConstants.TODO_PROJECT_ID_NOT_BLANK)
    private String projectId;
}
