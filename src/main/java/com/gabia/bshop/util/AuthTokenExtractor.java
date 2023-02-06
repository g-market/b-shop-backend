package com.gabia.bshop.util;

import org.springframework.stereotype.Component;

import com.gabia.bshop.exception.UnAuthorizedException;

@Component
public class AuthTokenExtractor {

	public String extractToken(final String authorizationHeader, final String tokenType) {
		if (authorizationHeader == null) {
			throw new UnAuthorizedException("토큰이 존재하지 않습니다.");
		}
		final String[] splitHeaders = authorizationHeader.split(" ");
		if (splitHeaders.length != 2 || !splitHeaders[0].equalsIgnoreCase(tokenType)) {
			throw new UnAuthorizedException("토큰이 잘못된 형식입니다.");
		}
		return splitHeaders[1];
	}
}
