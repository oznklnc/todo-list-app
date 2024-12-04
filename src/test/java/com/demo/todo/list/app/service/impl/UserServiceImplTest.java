package com.demo.todo.list.app.service.impl;


import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest extends UnitTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void should_create_or_update_user() {
        //given
        UserDocument userDocument = UserDocument.builder()
                .email("john@example.com")
                .build();

        when(userRepository.save(userDocument)).thenReturn(userDocument);

        //when
        UserDocument result = userService.createOrUpdate(userDocument);

        //then
        verify(userRepository).save(userDocument);
        assertThat(result).isNotNull();
        assertThat(result).isSameAs(userDocument);
    }

    @Test
    void should_exist_by_email() {
        //given
        String email = "john@example.com";
        when(userRepository.existsUserDocumentsByEmail(email)).thenReturn(true);

        //when
        boolean result = userService.existByEmail(email);

        //then
        verify(userRepository).existsUserDocumentsByEmail(email);
        assertThat(result).isTrue();
    }

    @Test
    void should_find_by_email() {
        //given
        String email = "john@example.com";
        UserDocument userDocument = UserDocument.builder()
                .email(email)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userDocument));

        //when
        UserDocument result = userService.findByEmail(email);

        //then
        verify(userRepository).findByEmail(email);
        assertThat(result).isNotNull();
        assertThat(result).isSameAs(userDocument);
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        //given
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userService.findByEmail(email))
                .isInstanceOf(ApiException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessageCode());


    }


}