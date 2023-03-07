package com.gabia.bshop.security.provider;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

import com.gabia.bshop.config.TokenProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RefreshTokenCookieProvider {

	public static final String REFRESH_TOKEN = "refreshToken";
	private static final int REMOVAL_MAX_AGE = 0;

	private final TokenProperties tokenProperties;

	public ResponseCookie createCookie(final String refreshToken) {
		return createTokenCookieBuilder(refreshToken)
			.maxAge(Duration.ofMillis(tokenProperties.getRefreshExpiredTime()))
			.build();
	}

	public ResponseCookie createLogoutCookie() {
		return createTokenCookieBuilder("")
			.maxAge(REMOVAL_MAX_AGE)
			.build();
	}

	private ResponseCookieBuilder createTokenCookieBuilder(final String value) {
		return ResponseCookie.from(REFRESH_TOKEN, value)
			.httpOnly(true)
			.path("/");
		// TODO: HTTPS
		// .secure(true)
		// .sameSite(SameSite.NONE.attributeValue());
	}
}
