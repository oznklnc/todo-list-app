package com.demo.todo.list.app.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConstants {

    //USER RELATED EXCEPTIONS
    public static final String USER_FIRST_NAME_REQUIRED = "user.firstName.not.blank";
    public static final String USER_LAST_NAME_REQUIRED = "user.lastName.not.blank";
    public static final String PASSWORD_REQUIRED = "user.password.not.blank";
    public static final String EMAIL_REQUIRED = "user.email.not.blank";
    public static final String EMAIL_FORMAT = "user.email.format";
    public static final String USER_NAME_REQUIRED = "user.username.not.blank";


    public static final String USER_NOT_FOUND = "user.not.found";
    public static final String USER_ALREADY_EXISTS = "user.already.exists";

    //PROJECT RELATED EXCEPTIONS
    public static final String PROJECT_NAME_NOT_BLANK = "project.name.not.blank";
    public static final String PROJECT_DESCRIPTION_NOT_BLANK = "project.description.not.blank";

    public static final String PROJECT_NOT_FOUND = "project.not.found";
    public static final String PROJECT_ALREADY_EXISTS = "project.already.exists";

    //TODOS RELATED EXCEPTIONS
    public static final String TODO_TITLE_NOT_BLANK = "todo.title.not.blank";
    public static final String TODO_PROJECT_ID_NOT_BLANK = "todo.projectId.not.blank";

    public static final String TODO_NOT_FOUND = "todo.not.found";

    //CREDENTIALS RELATED EXCEPTIONS
    public static final String INVALID_CREDENTIALS = "invalid.credentials";
    public static final String INVALID_TOKEN = "invalid.token";

    //METHOD EXCEPTIONS
    public static final String METHOD_NOT_ALLOWED = "method.not.allowed";
    public static final String METHOD_UNSUPPORTED = "method.unsupported";
    public static final String METHOD_NOT_FOUND = "method.not.found";
    public static final String NOT_ACCEPTABLE =  "not.acceptable";
    public static final String INTERNAL_SERVER_ERROR = "internal.server.error";

}
