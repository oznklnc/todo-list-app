package com.demo.todo.list.app.model.enums;

import com.demo.todo.list.app.constant.ExceptionConstants;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //USER RELATED EXCEPTIONS
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionConstants.USER_NOT_FOUND),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, ExceptionConstants.USER_ALREADY_EXISTS),

    //PROJECT RELATED EXCEPTIONS
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionConstants.PROJECT_NOT_FOUND),
    PROJECT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, ExceptionConstants.PROJECT_ALREADY_EXISTS),

    //TODOREQUEST RELATED EXCEPTIONS
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionConstants.TODO_NOT_FOUND),


    //CREDENTIALS RELATED EXCEPTIONS
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, ExceptionConstants.INVALID_CREDENTIALS),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, ExceptionConstants.INVALID_TOKEN),

    //METHOD EXCEPTIONS
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ExceptionConstants.METHOD_NOT_ALLOWED),
    METHOD_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionConstants.METHOD_NOT_FOUND),
    METHOD_UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ExceptionConstants.METHOD_UNSUPPORTED),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, ExceptionConstants.NOT_ACCEPTABLE),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionConstants.INTERNAL_SERVER_ERROR)
    ;


    private final HttpStatus httpStatus;
    private final String messageCode;

    ErrorCode(HttpStatus httpStatus, String messageCode) {
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
    }

}
