package com.gabia.bshop.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomException {

    public UnAuthorizedException(final String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}