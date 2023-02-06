package com.gabia.bshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorResponse errorResponse;

    protected CustomException(HttpStatus status, String message) {
        this.status = status;
        this.errorResponse = new ErrorResponse(message);
    }
}
