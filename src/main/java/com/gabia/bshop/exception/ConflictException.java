package com.gabia.bshop.exception;

public class ConflictException extends ApplicationException {

	public ConflictException(final ErrorCode errorCode) {
		super(errorCode);
	}

	public ConflictException(final ErrorCode errorCode, final Object... parameter) {
		super(errorCode, parameter);
	}
}
