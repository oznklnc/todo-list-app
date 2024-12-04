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
public class UserLoginRequest {

    @NotBlank(message = ExceptionConstants.USER_NAME_REQUIRED)
    private String username;

    @NotBlank(message = ExceptionConstants.PASSWORD_REQUIRED)
    @ToString.Exclude
    private String password;
}
