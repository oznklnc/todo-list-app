package com.demo.todo.list.app.util;

import com.demo.todo.list.app.base.UnitTest;
import com.demo.todo.list.app.model.dto.ApiErrorDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class MessageSourceUtilTest extends UnitTest {

    @Mock
    MessageSource messageSource;
    @InjectMocks
    MessageSourceUtil messageSourceUtil;

    @Test
    void should_get_message() {
        //given
        String messageCode = "messageCode";
        String message = "code;message";
        when(messageSource.getMessage(eq(messageCode), any(), any())).thenReturn(message);

        //when
        ApiErrorDto apiErrorDto = messageSourceUtil.getMessage(messageCode);

        //then
        assertThat(apiErrorDto).isNotNull();
        assertThat(apiErrorDto.getCode()).isEqualTo("code");
        assertThat(apiErrorDto.getMessage()).isEqualTo("message");
    }
}