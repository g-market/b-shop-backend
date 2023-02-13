package com.gabia.bshop.util;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.stereotype.Component;

import com.gabia.bshop.exception.UnAuthorizedException;

@Component
public class AuthTokenExtractor {

	private static final int VALID_AUTHORIZATION_HEADER_LENGTH = 2;
	private static final int TOKEN_TYPE_INDEX = 0;
	private static final int ACCESS_TOKEN_INDEX = 1;

	public String extractToken(final String authorizationHeader, final String tokenType) {
		if (authorizationHeader == null) {
			throw new UnAuthorizedException(TOKEN_NOT_EXIST_EXCEPTION);
		}
		final String[] splitHeaders = authorizationHeader.split(" ");
		if (splitHeaders.length != VALID_AUTHORIZATION_HEADER_LENGTH
			|| !splitHeaders[TOKEN_TYPE_INDEX].equalsIgnoreCase(tokenType)) {
			throw new UnAuthorizedException(TOKEN_INVALID_FORMAT_EXCEPTION);
		}
		return splitHeaders[ACCESS_TOKEN_INDEX];
	}
}
