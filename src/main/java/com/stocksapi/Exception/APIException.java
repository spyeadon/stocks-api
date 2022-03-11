package com.stocksapi.Exception;

public class APIException extends RuntimeException {
    APIException(String message) {
        super(message);
    }
}
