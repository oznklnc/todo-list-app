package com.demo.todo.list.app.security.service;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import com.demo.todo.list.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


class TodoListUserDetailsServiceTest extends UnitTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TodoListUserDetailsService todoListUserDetailsService;

    @Test
    void should_load_user_by_username() {
        //given
        String username = "john.doe@example.com";
        UserDocument userDocument = UserDocument.builder()
                .id("test")
                .firstName("John")
                .lastName("Doe")
                .email(username)
                .build();

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userDocument));

        //when
        UserDetails userDetails = todoListUserDetailsService.loadUserByUsername(username);

        //then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(TodoListUserDetails.class);
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.getUsername()).isEqualTo(userDocument.getEmail());
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        //given
        String username = "test@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> todoListUserDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
}