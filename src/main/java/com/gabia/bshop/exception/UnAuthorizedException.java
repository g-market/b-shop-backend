package com.gabia.bshop.exception;

public class UnAuthorizedException extends ApplicationException {

	public UnAuthorizedException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
