package com.demo.todo.list.app.annotation;

import com.demo.todo.list.app.base.config.TestUserDetailsSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestUserDetailsSecurityContextFactory.class)
public @interface WithMockTodoUser {

    String username() default "user";
    String password() default "pass";
}
