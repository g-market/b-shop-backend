package com.gabia.bshop.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException {

    public ConflictException(final String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
