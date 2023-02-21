package com.gabia.bshop.exception;

public class InternalServerException extends ApplicationException {

	public InternalServerException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
