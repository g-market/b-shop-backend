package com.gabia.bshop.exception;

public class ForbiddenException extends ApplicationException {

	public ForbiddenException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
