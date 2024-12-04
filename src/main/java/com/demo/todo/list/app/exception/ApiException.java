package com.demo.todo.list.app.exception;

import com.demo.todo.list.app.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private HttpStatus httpStatus;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessageCode());
        this.errorCode = errorCode;
    }

    public ApiException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessageCode());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
