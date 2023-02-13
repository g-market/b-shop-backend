package com.gabia.bshop.exception;

import lombok.Builder;

@Builder
public record ExceptionResponse(
	String message
) {
}
