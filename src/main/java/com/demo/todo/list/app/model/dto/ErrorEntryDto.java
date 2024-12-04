package com.demo.todo.list.app.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class ErrorEntry {

    private final String errorCode;
    private final String message;
}
