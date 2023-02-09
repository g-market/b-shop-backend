package com.gabia.bshop.exception;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final HttpStatus status;
	private final ErrorResponse errorResponse;

	protected CustomException(final HttpStatus status, final String message) {
		this.status = status;
		this.errorResponse = new ErrorResponse(message);
	}
}
