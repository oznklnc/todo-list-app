package com.demo.todo.list.app.constant;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class SwaggerConstants {

    public static final String CREATED = String.valueOf(HttpStatus.CREATED.value());
}
