package com.gabia.bshop.fixture;

import java.nio.charset.StandardCharsets;

import com.gabia.bshop.config.TokenProperties;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenPropertiesFixture {

	public static final String VALID_SECRET = "TESTSECRETKEYTESTSECRETKEYTESTSECRETKEYTESTSECRETKEY";
	public static final String INVALID_SECRET = "INVALIDTESTSECRETKEYTESTSECRETKEYTESTSECRETKEYTESTSECRETKEY";
	public static final long VALID_ACCESS_EXPIRED_TIME = 3600000L;
	public static final long INVALID_ACCESS_EXPIRED_TIME = 0L;
	public static final long VALID_REFRESH_EXPIRED_TIME = 2420000000L;

	public static final TokenProperties VALID_TOKEN_PROPERTIES;
	public static final TokenProperties INVALID_TOKEN_PROPERTIES;
	public static final TokenProperties EXPIRED_ACCESS_TOKEN_PROPERTIES;

	static {
		VALID_TOKEN_PROPERTIES = getValidTokenProperties();
		INVALID_TOKEN_PROPERTIES = getInvalidTokenProperties();
		EXPIRED_ACCESS_TOKEN_PROPERTIES = getExpiredAccessTokenProperties();
	}

	private static TokenProperties getTokenProperties() {
		return new TokenProperties();
	}

	private static TokenProperties getValidTokenProperties() {
		final TokenProperties tokenProperties = getTokenProperties();
		tokenProperties.setSecret(VALID_SECRET);
		tokenProperties.setAccessExpiredTime(VALID_ACCESS_EXPIRED_TIME);
		tokenProperties.setRefreshExpiredTime(VALID_REFRESH_EXPIRED_TIME);
		tokenProperties.setSecretKey(Keys.hmacShaKeyFor(VALID_SECRET.getBytes(StandardCharsets.UTF_8)));
		return tokenProperties;
	}

	private static TokenProperties getInvalidTokenProperties() {
		final TokenProperties tokenProperties = getTokenProperties();
		tokenProperties.setSecret(INVALID_SECRET);
		tokenProperties.setAccessExpiredTime(VALID_ACCESS_EXPIRED_TIME);
		tokenProperties.setRefreshExpiredTime(VALID_REFRESH_EXPIRED_TIME);
		tokenProperties.setSecretKey(Keys.hmacShaKeyFor(INVALID_SECRET.getBytes(StandardCharsets.UTF_8)));
		return tokenProperties;
	}

	private static TokenProperties getExpiredAccessTokenProperties() {
		final TokenProperties tokenProperties = getTokenProperties();
		tokenProperties.setSecret(VALID_SECRET);
		tokenProperties.setAccessExpiredTime(INVALID_ACCESS_EXPIRED_TIME);
		tokenProperties.setSecretKey(Keys.hmacShaKeyFor(VALID_SECRET.getBytes(StandardCharsets.UTF_8)));
		return tokenProperties;
	}
}
