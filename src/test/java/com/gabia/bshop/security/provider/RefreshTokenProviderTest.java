package com.gabia.bshop.security.provider;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gabia.bshop.fixture.TokenPropertiesFixture;
import com.gabia.bshop.security.RefreshToken;

class RefreshTokenProviderTest {

	@Test
	@DisplayName("리프레시 토큰을 생성한다")
	void createRefreshToken() {
		// given
		RefreshTokenProvider provider = new RefreshTokenProvider(TokenPropertiesFixture.VALID_TOKEN_PROPERTIES);
		// when
		RefreshToken token = provider.createToken(1L);
		//then
		assertAll(
			() -> assertThat(token.isExpired()).isFalse(),
			() -> assertThat(token.refreshToken()).isNotNull()
		);
	}
}
