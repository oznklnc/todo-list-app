package com.demo.todo.list.app.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDto {

    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;
    private String message;

}
