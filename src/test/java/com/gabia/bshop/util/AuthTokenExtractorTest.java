package com.gabia.bshop.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gabia.bshop.exception.UnAuthorizedException;

class AuthTokenExtractorTest {

	private final AuthTokenExtractor authTokenExtractor = new AuthTokenExtractor();

	@Test
	@DisplayName("Authorization 헤더에서 토큰을 추출한다")
	void given_header_when_extractToken_then_return_extracted_token() {
		// given
		String authorizationHeader = "Bearer token";
		// when
		String token = authTokenExtractor.extractToken(authorizationHeader, "Bearer");
		// then
		assertThat(token).isEqualTo("token");
	}

	@Test
	@DisplayName("Authorization 헤더가 null 이면 예외가 발생한다")
	void given_null_header_when_extractToken_then_throw_exception() {
		assertThatThrownBy(() -> authTokenExtractor.extractToken(null, "Bearer"))
			.isExactlyInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("Authorization 헤더의 타입이 불일치하면 예외가 발생한다")
	void given_another_token_type_header_when_extractToken_then_throw_exception() {
		// given
		String authorizationHeader = "ANOTHER_TOKEN_TYPE token";

		// when & then
		assertThatThrownBy(() -> authTokenExtractor.extractToken(authorizationHeader, "Bearer"))
			.isExactlyInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("Authorization 헤더의 형식이 불일치하면 예외가 발생한다")
	void given_invalid_token_header_when_extractToken_then_throw_exception() {
		// given
		String authorizationHeader = "Bearer token token2";

		// when & then
		assertThatThrownBy(() -> authTokenExtractor.extractToken(authorizationHeader, "Bearer"))
			.isExactlyInstanceOf(UnAuthorizedException.class);
	}
}
