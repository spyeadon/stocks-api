package com.stocksapi.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String exceptionType;
    private String message;
    private String fieldName;
}