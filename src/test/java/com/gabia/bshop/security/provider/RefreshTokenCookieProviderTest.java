package com.gabia.bshop.security.provider;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class RefreshTokenCookieProviderTest {

	private final RefreshTokenCookieProvider provider =
		new RefreshTokenCookieProvider(1209600000L);

	@Test
	void 리프레시_토큰으로_쿠키를_생성한다() {
		// given
		final String refreshTokenValue = "refreshTokenValue";
		// when
		ResponseCookie responseCookie = provider.createCookie(refreshTokenValue);
		// then
		assertAll(
			() -> assertThat(responseCookie.getValue()).isEqualTo(refreshTokenValue),
			() -> assertThat(responseCookie.getMaxAge()).isEqualTo(
				Duration.ofMillis(1209600000L))
		);
	}

	@Test
	void 생성된_쿠키를_만료시킨_뒤_반환한다() {
		// when
		ResponseCookie responseCookie = provider.createLogoutCookie();
		// then
		assertThat(responseCookie.getMaxAge()).isZero();
	}
}
