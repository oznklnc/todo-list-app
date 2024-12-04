package com.demo.todo.list.app.initializer;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.error.context.ErrorContext;
import com.demo.todo.list.app.model.dto.ErrorEntryDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static com.demo.todo.list.app.constant.TodoListConstants.UNKNOWN;


@Getter
@RequiredArgsConstructor
public class CouchbaseError {

    private final List<ErrorEntryDto> errorEntries;

    public static CouchbaseError create(@NotNull CouchbaseException exception) {
        ErrorContext context = exception.context();
        if (context == null) {
            return unknown(exception);
        }
        Map<String, Object> exported = new HashMap<>();
        context.injectExportableParams(exported);
        Object errors = exported.get("errors");
        if (!(errors instanceof List<?>)) {
            return unknown(exception);
        }
        List<ErrorEntryDto> entries = new ArrayList<>();
        for (Object errorObject : (List<?>) errors) {
            if (!(errorObject instanceof Map<?, ?>)) {
                return unknown(exception);
            }
            Map<?, ?> errorMap = (Map<?, ?>) errorObject;
            Object errorCode = errorMap.get("code");
            if (errorCode == null) {
                continue;
            }
            entries.add(new ErrorEntryDto(errorCode.toString(), String.valueOf(errorMap.get("message"))));
        }
        return new CouchbaseError(entries);
    }

    private static CouchbaseError unknown(@NotNull CouchbaseException exception) {
        return new CouchbaseError(
                Collections.singletonList(new ErrorEntryDto(UNKNOWN, exception.toString()))
        );
    }

    public ErrorEntryDto getFirstError() {
        return errorEntries.stream()
                .findFirst()
                .orElse(null);
    }
}
