package com.demo.todo.list.app.service.impl;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;


class SecurityContextServiceImplTest extends UnitTest {

    @InjectMocks
    SecurityContextServiceImpl securityContextService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_get_user_id() {
        //given
        UserDocument userDocument = UserDocument.builder()
                .id("test")
                .email("john.doe@example.com")
                .build();
        TodoListUserDetails todoListUserDetails = new TodoListUserDetails(userDocument);
        Authentication authentication = new UsernamePasswordAuthenticationToken(todoListUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        String userId = securityContextService.getUserId();

        //then
        assertThat(userId).isNotNull();
        assertThat(userId).isEqualTo(userDocument.getId());
    }

    @Test
    void should_get_user() {
        //given
        UserDocument userDocument = UserDocument.builder()
                .id("test")
                .email("john.doe@example.com")
                .build();
        TodoListUserDetails todoListUserDetails = new TodoListUserDetails(userDocument);
        Authentication authentication = new UsernamePasswordAuthenticationToken(todoListUserDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        UserDocument result = securityContextService.getUser();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isSameAs(userDocument);
    }


    @Test
    void should_return_null_when_authentication_is_null() {
        //when
        String userId = securityContextService.getUserId();

        //then
        assertThat(userId).isNull();
    }

}