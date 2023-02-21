package com.gabia.bshop.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends ApplicationException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
