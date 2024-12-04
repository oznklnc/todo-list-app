package com.demo.todo.list.app.security.jwt.impl;


import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.config.properties.SecurityProperty;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JWTHelperImplTest extends UnitTest {

    @Mock
    SecurityProperty securityProperty;
    JWTHelperImpl jwtHelper;

    static final String USER_NAME = "john.doe@example.com";


    @BeforeEach
    void setUp() {
        when(securityProperty.getSecretKey()).thenReturn("bvkdmvldgjkdnjvsvnjdnvjfnbvmngsmögnsgmönsgösngksndgkdnbkdnbjdngndkgndklbmkdlhbmlk");
        when(securityProperty.getDuration()).thenReturn(Duration.ofMinutes(15));
        jwtHelper = new JWTHelperImpl(securityProperty);
    }

    @Test
    void should_validate_token() {
        //given
        String jwtToken = jwtHelper.generateJwtToken(getAuthentication());
        //when
        boolean isValid = jwtHelper.validateToken(jwtToken);
        //then
        assertThat(isValid).isTrue();
    }

    @Test
    void should_not_validate_token() {
        //given
        String jwtToken = jwtHelper.generateJwtToken(getAuthentication());
        //when
        boolean isValid = jwtHelper.validateToken(jwtToken + "invalid");
        //then
        assertThat(isValid).isFalse();
    }

    @Test
    void should_get_username() {
        //given
        String jwtToken = jwtHelper.generateJwtToken(getAuthentication());
        //when
        String username = jwtHelper.getUsername(jwtToken);
        //then
        assertThat(username).isEqualTo(USER_NAME);
    }

    @Test
    void should_not_get_username() {
        //given
        String jwtToken = jwtHelper.generateJwtToken(getAuthentication());
        //when
        String username = jwtHelper.getUsername(jwtToken + "invalid");
        //then
        assertThat(username).isNull();
    }

    @Test
    void generateJwtToken() {
        //given
        Authentication authentication = getAuthentication();
        //when
        String jwtToken = jwtHelper.generateJwtToken(authentication);
        //then
        assertThat(jwtToken).isNotNull();
    }

    private Authentication getAuthentication() {
        UserDocument userDocument = UserDocument.builder()
                .email(USER_NAME)
                .build();
        TodoListUserDetails userDetails = new TodoListUserDetails(userDocument);
        return new UsernamePasswordAuthenticationToken(userDetails, null, null);
    }

}