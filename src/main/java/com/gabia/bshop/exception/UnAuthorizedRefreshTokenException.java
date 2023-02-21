package com.gabia.bshop.exception;

public class UnAuthorizedRefreshTokenException extends ApplicationException {

	public UnAuthorizedRefreshTokenException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
