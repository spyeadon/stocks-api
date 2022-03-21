package com.stocksapi.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class APIException extends RuntimeException {
    private HttpStatus status;
    public APIException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
