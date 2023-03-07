package com.gabia.bshop.exception;

public class NotFoundException extends ApplicationException {

	public NotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}

	public NotFoundException(final ErrorCode errorCode, final Object... parameter) {
		super(errorCode, parameter);
	}
}
