package com.demo.todo.list.app.util;

import com.demo.todo.list.app.constant.TodoListConstants;
import com.demo.todo.list.app.model.dto.ApiErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MessageSourceUtil {

    private final MessageSource messageSource;

    public ApiErrorDto getMessage(String messageCode) {
        String message = messageSource.getMessage(messageCode, null, LocaleContextHolder.getLocale());
        String[] split = message.split(TodoListConstants.DELIMITER);
        return ApiErrorDto.builder()
                .code(split[0])
                .message(split[1])
                .build();
    }
}
