package com.demo.todo.list.app.mapper;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.entity.UserDocument;
import com.demo.todo.list.app.model.request.UserRegisterRequest;
import com.demo.todo.list.app.model.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


class UserMapperTest extends UnitTest {

    @InjectMocks
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void should_map_user_document() {
        //given
        String encodedPass = "encodedPass";
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("test@yopmail.com")
                .firstName("test")
                .password("test")
                .build();
        when(passwordEncoder.encode(userRegisterRequest.getPassword())).thenReturn(encodedPass);

        //when
        UserDocument userDocument = userMapper.toUserDocument(userRegisterRequest);

        //then
        assertThat(userDocument).isNotNull();
        assertThat(userDocument.getEmail()).isEqualTo(userRegisterRequest.getEmail());
        assertThat(userDocument.getFirstName()).isEqualTo(userRegisterRequest.getFirstName());
        assertThat(userDocument.getPassword()).isEqualTo(encodedPass);

    }

    @Test
    void should_return_user_response() {
        //given
        UserDocument userDocument = UserDocument.builder()
                .id("test")
                .email("john.doe@example.com")
                .firstName("john")
                .lastName("doe")
                .build();

        //when
        UserResponse userResponse = userMapper.toUserResponse(userDocument);

        //then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isEqualTo(userDocument.getId());
        assertThat(userResponse.getEmail()).isEqualTo(userDocument.getEmail());
        assertThat(userResponse.getFirstName()).isEqualTo(userDocument.getFirstName());
        assertThat(userResponse.getLastName()).isEqualTo(userDocument.getLastName());
    }
}