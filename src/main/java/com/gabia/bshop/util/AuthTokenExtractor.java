package com.gabia.bshop.util;

import org.springframework.stereotype.Component;

import com.gabia.bshop.exception.UnAuthorizedException;

@Component
public class AuthTokenExtractor {

	private static final int VALID_AUTHORIZATION_HEADER_LENGTH = 2;
	private static final int TOKEN_TYPE_INDEX = 0;
	private static final int ACCESS_TOKEN_INDEX = 1;

	public String extractToken(final String authorizationHeader, final String tokenType) {
		if (authorizationHeader == null) {
			throw new UnAuthorizedException("토큰이 존재하지 않습니다.");
		}
		final String[] splitHeaders = authorizationHeader.split(" ");
		if (splitHeaders.length != VALID_AUTHORIZATION_HEADER_LENGTH
			|| !splitHeaders[TOKEN_TYPE_INDEX].equalsIgnoreCase(tokenType)) {
			throw new UnAuthorizedException("토큰이 잘못된 형식입니다.");
		}
		return splitHeaders[ACCESS_TOKEN_INDEX];
	}
}
