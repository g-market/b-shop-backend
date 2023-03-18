package com.gabia.bshop.security.provider;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.exception.UnAuthorizedException;
import com.gabia.bshop.fixture.TokenPropertiesFixture;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.util.AuthTokenExtractor;

class JwtProviderTest {

	private final JwtProvider jwtProvider;

	private final FakeJwtProvider fakeJwtProvider;

	private JwtProviderTest() {
		this.jwtProvider = new JwtProvider(new AuthTokenExtractor(), TokenPropertiesFixture.VALID_TOKEN_PROPERTIES);
		this.fakeJwtProvider = new FakeJwtProvider(TokenPropertiesFixture.VALID_TOKEN_PROPERTIES);
	}

	@Test
	@DisplayName("토큰을 생성한다")
	void createToken() {
		// given
		final Long memberId = 1L;
		// when
		final String token = jwtProvider.createAccessToken(memberId, MemberRole.NORMAL);
		// then
		assertThat(token).isNotNull();
	}

	@Test
	@DisplayName("토큰이 유효성을 판단여부를 검증한다")
	void isValidToken() {
		// given
		String accessToken = jwtProvider.createAccessToken(1L, MemberRole.NORMAL);
		String authorizationHeader = "Bearer " + accessToken;

		// when & then
		assertThat(jwtProvider.isValidToken(authorizationHeader)).isTrue();
	}

	@Test
	@DisplayName("토큰 유효기간이 지난 경우 이를 검증한다")
	void isExpiredToken() {
		// given
		JwtProvider jwtProvider = new JwtProvider(new AuthTokenExtractor(),
			TokenPropertiesFixture.EXPIRED_ACCESS_TOKEN_PROPERTIES);
		String accessToken = jwtProvider.createAccessToken(1L, MemberRole.NORMAL);
		String authorizationHeader = "Bearer " + accessToken;

		// when, then
		assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
	}

	@Test
	@DisplayName("토큰의 형식이 틀린 경우 이를 검증한다")
	void isInvalidTokenType() {
		// given
		String authorizationHeader = "Bearer invalidToken";

		// when, then
		assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
	}

	@Test
	@DisplayName("토큰의 시크릿 키가 틀린 경우 이를 검증한다")
	void isInvalidSecretKey() {
		// given
		JwtProvider invalidJwtProvider = new JwtProvider(new AuthTokenExtractor(),
			TokenPropertiesFixture.INVALID_TOKEN_PROPERTIES);
		String token = invalidJwtProvider.createAccessToken(1L, MemberRole.NORMAL);
		String authorizationHeader = "Bearer " + token;
		// when & then
		assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
	}

	@Test
	@DisplayName("토큰의 payload를 복호화한다")
	void getPayloadFromToken() {
		// given
		String token = jwtProvider.createAccessToken(1L, MemberRole.NORMAL);
		String authorizationHeader = "Bearer " + token;
		// when
		MemberPayload payload = jwtProvider.getPayload(authorizationHeader);
		// then
		assertThat(payload).isEqualTo(new MemberPayload(1L, MemberRole.NORMAL));
	}

	@Test
	@DisplayName("토큰의 payload 복호화시 Long 자료구조가 아닌 id 이면 예외를 반환한다")
	void isInvalidTokenIdThenThrowException() {
		// given
		String token = fakeJwtProvider.createAccessToken("string", MemberRole.NORMAL);
		String authorizationHeader = "Bearer " + token;
		// when, then
		assertThatThrownBy(() -> jwtProvider.getPayload(authorizationHeader))
			.isExactlyInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("토큰의 payload 복호화시 필요한 정보가 없으면 예외를 반환한다")
	void isInvalidTokenValueThenThrowException() {
		// given
		String accessToken = fakeJwtProvider.createAccessToken(1L);
		String authorizationHeader = "Bearer " + accessToken;

		// when, then
		assertThatThrownBy(() -> jwtProvider.getPayload(authorizationHeader))
			.isExactlyInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("토큰의 payload 복호화 시 role값이 올바르지 않으면 예외를 반환한다")
	void isInvalidRoleThenThrowException() {
		// given
		String token = fakeJwtProvider.createAccessToken(1L, "invalid");
		String authorizationHeader = "Bearer " + token;

		// when, then
		assertThatThrownBy(() -> jwtProvider.getPayload(authorizationHeader))
			.isExactlyInstanceOf(UnAuthorizedException.class);
	}
}
