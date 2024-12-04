package com.demo.todo.list.app.security;


import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class TodoListAuthenticationProviderTest extends UnitTest {

    @Mock
    UserDetailsService userDetailsService;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    TodoListAuthenticationProvider todoListAuthenticationProvider;

    @Test
    void should_authenticate() {
        //given
        Authentication authentication = Mockito.mock(Authentication.class);
        String username = "john.doe@example.com";
        String password = "password";
        when(authentication.getName()).thenReturn(username);
        when(authentication.getCredentials()).thenReturn(password);
        UserDocument userDocument = UserDocument.builder()
                .email(username)
                .password(password)
                .build();
        TodoListUserDetails userDetails = new TodoListUserDetails(userDocument);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        //when
        Authentication authenticate = todoListAuthenticationProvider.authenticate(authentication);

        //then
        assertThat(authenticate).isNotNull();
        assertThat(authenticate).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authenticate.getPrincipal()).isEqualTo(userDetails);
        assertThat(authenticate.getCredentials()).isEqualTo(password);
    }

    @Test
    void should_not_authenticate() {
        //given
        Authentication authentication = Mockito.mock(Authentication.class);
        String username = "john.doe@example.com";
        String password = "password";
        when(authentication.getName()).thenReturn(username);
        when(authentication.getCredentials()).thenReturn(password);
        UserDocument userDocument = UserDocument.builder()
                .email(username)
                .password("differentPassword")
                .build();
        TodoListUserDetails userDetails = new TodoListUserDetails(userDocument);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> todoListAuthenticationProvider.authenticate(authentication))
                .isInstanceOf(org.springframework.security.authentication.BadCredentialsException.class)
                .hasMessage("Invalid password");
    }

    @Test
    void should_support() {
        //given
        Class<?> authentication = UsernamePasswordAuthenticationToken.class;

        //when
        boolean support = todoListAuthenticationProvider.supports(authentication);

        //then
        assertThat(support).isTrue();
    }
}