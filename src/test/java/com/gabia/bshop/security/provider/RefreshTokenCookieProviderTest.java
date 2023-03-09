package com.gabia.bshop.security.provider;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import com.gabia.bshop.fixture.TokenPropertiesFixture;

class RefreshTokenCookieProviderTest {

	private final RefreshTokenCookieProvider provider =
		new RefreshTokenCookieProvider(TokenPropertiesFixture.VALID_TOKEN_PROPERTIES);

	@Test
	@DisplayName("리프레시 토큰으로 쿠키를 생성한다")
	void createCookieByRefreshToken() {
		// given
		final String refreshTokenValue = "refreshTokenValue";
		// when
		ResponseCookie responseCookie = provider.createCookie(refreshTokenValue);
		// then
		assertAll(
			() -> assertThat(responseCookie.getValue()).isEqualTo(refreshTokenValue),
			() -> assertThat(responseCookie.getMaxAge()).isEqualTo(
				Duration.ofMillis(TokenPropertiesFixture.VALID_REFRESH_EXPIRED_TIME))
		);
	}

	@Test
	@DisplayName("쿠키를 만료 시킨뒤에 이를 반환한다")
	void createLogoutCookie() {
		// when
		ResponseCookie responseCookie = provider.createLogoutCookie();
		// then
		assertThat(responseCookie.getMaxAge()).isZero();
	}
}
