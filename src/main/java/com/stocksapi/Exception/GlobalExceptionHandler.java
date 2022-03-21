package com.stocksapi.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> handleAPIException(APIException exception) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(exception.getMessage())
                        .exceptionType(exception.getClass().getSimpleName())
                    .build(),
                exception.getStatus()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleBadRequest(MethodArgumentNotValidException exception) {
        List<ErrorResponse> exceptionResponse = exception.getBindingResult().getAllErrors()
                .stream()
                .map(error -> {
                    ErrorResponse errorResponse = ErrorResponse.builder()
                        .exceptionType(error.getClass().getSimpleName())
                        .message(error.getDefaultMessage())
                    .build();
                    if (error instanceof FieldError) errorResponse.setFieldName(((FieldError) error).getField());
                    return errorResponse;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}