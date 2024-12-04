package com.demo.todo.list.app.model.request;


import com.demo.todo.list.app.constant.ExceptionConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterRequest {

    @NotBlank(message = ExceptionConstants.USER_FIRST_NAME_REQUIRED)
    private String firstName;

    @NotBlank(message = ExceptionConstants.USER_LAST_NAME_REQUIRED)
    private String lastName;

    @NotBlank(message = ExceptionConstants.EMAIL_REQUIRED)
    @Email(message = ExceptionConstants.EMAIL_FORMAT)
    private String email;

    @NotBlank(message = ExceptionConstants.PASSWORD_REQUIRED)
    @ToString.Exclude
    private String password;
}
