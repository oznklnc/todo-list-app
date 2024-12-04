package com.demo.todo.list.app.handler;

import com.demo.todo.list.app.exception.ApiException;
import com.demo.todo.list.app.model.dto.ApiErrorDto;
import com.demo.todo.list.app.model.enums.ErrorCode;
import com.demo.todo.list.app.model.response.ApiErrorResponse;
import com.demo.todo.list.app.util.MessageSourceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSourceUtil messageSourceUtil;
    private final HttpServletRequest httpServletRequest;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception) {
        log.error("Exception Occurred ", exception);
        ErrorCode internalServerError = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiErrorDto message = messageSourceUtil.getMessage(internalServerError.getMessageCode());
        return ResponseEntity.status(internalServerError.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .createdDate(new Date())
                        .url(httpServletRequest.getRequestURI())
                        .messages(List.of(message))
                        .build());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception) {
        log.error("ApiException Occurred ", exception);
        HttpStatus httpStatus = getHttpStatusOrDefault(exception.getHttpStatus());
        ApiErrorDto message = messageSourceUtil.getMessage(exception.getErrorCode().getMessageCode());
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .url(httpServletRequest.getRequestURI())
                .createdDate(new Date())
                .messages(List.of(message))
                .build();
        return ResponseEntity.status(httpStatus).body(apiErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        log.error("MethodArgumentNotValidException Occurred {}", ex.getMessage());

        List<ApiErrorDto> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> {
                    ApiErrorDto message = messageSourceUtil.getMessage(fieldError.getDefaultMessage());
                    message.setField(fieldError.getField());
                    return message;
                })
                .toList();

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .createdDate(new Date())
                .url(httpServletRequest.getRequestURI())
                .messages(errorMessages)
                .build();
        return ResponseEntity.badRequest().body(apiErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        log.error("HttpRequestMethodNotSupportedException Occurred ", ex);
        ErrorCode methodNotAllowed = ErrorCode.METHOD_NOT_ALLOWED;
        ApiErrorDto message = messageSourceUtil.getMessage(methodNotAllowed.getMessageCode());
        return ResponseEntity.status(methodNotAllowed.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .createdDate(new Date())
                        .url(httpServletRequest.getRequestURI())
                        .messages(List.of(message))
                        .build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatusCode status,
                                                                     WebRequest request) {
        log.error("HttpMediaTypeNotSupportedException Occurred ", ex);
        ErrorCode methodUnsupported = ErrorCode.METHOD_UNSUPPORTED_MEDIA_TYPE;
        ApiErrorDto message = messageSourceUtil.getMessage(methodUnsupported.getMessageCode());

        return ResponseEntity.status(methodUnsupported.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .createdDate(new Date())
                        .url(httpServletRequest.getRequestURI())
                        .messages(List.of(message))
                        .build());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode status,
                                                                   WebRequest request) {
        log.error("NoHandlerFoundException Occurred ", ex);
        ErrorCode methodNotFound = ErrorCode.METHOD_NOT_FOUND;
        ApiErrorDto message = messageSourceUtil.getMessage(methodNotFound.getMessageCode());
        return ResponseEntity.status(methodNotFound.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .createdDate(new Date())
                        .url(httpServletRequest.getRequestURI())
                        .messages(List.of(message))
                        .build());
    }



    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatusCode status,
                                                                      WebRequest request) {
        log.error("HttpMediaTypeNotAcceptableException Occurred ", ex);
        ErrorCode notAcceptable = ErrorCode.NOT_ACCEPTABLE;
        ApiErrorDto message = messageSourceUtil.getMessage(notAcceptable.getMessageCode());
        return ResponseEntity.status(notAcceptable.getHttpStatus())
                .body(ApiErrorResponse.builder()
                        .createdDate(new Date())
                        .url(httpServletRequest.getRequestURI())
                        .messages(List.of(message))
                        .build());
    }


    private HttpStatus getHttpStatusOrDefault(HttpStatus httpStatus) {
        return Optional.ofNullable(httpStatus).orElse(HttpStatus.BAD_REQUEST);
    }
}
