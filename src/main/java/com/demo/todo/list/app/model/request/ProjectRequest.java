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
public class ProjectRequest {

    @NotBlank(message = ExceptionConstants.PROJECT_NAME_NOT_BLANK)
    private String name;
    @NotBlank(message = ExceptionConstants.PROJECT_DESCRIPTION_NOT_BLANK)
    private String description;
}
