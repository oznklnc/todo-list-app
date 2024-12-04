package com.demo.todo.list.app.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorEntryDto {

    private final String errorCode;
    private final String message;
}
