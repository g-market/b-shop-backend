package com.gabia.bshop.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.gabia.bshop.exception.UnAuthorizedRefreshTokenException;
import com.gabia.bshop.security.RefreshToken;

@DisplayName("[Redis] RefreshToken Repository 테스트")
// class TokenRepositoryImplTest extends IntegrationTest {
@SpringBootTest
class RefreshTokenRepositoryImplTest {

	private final String tokenValue = "tokenValue";
	private final Long memberId = 1L;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Value("${token.refresh-expired-time}")
	private long expireLength;

	private final LocalDateTime expiredAt = LocalDateTime.now().plusDays(TimeUnit.MILLISECONDS.toDays(expireLength));

	@BeforeEach
	void setUp() {
		final RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
		connectionFactory.getConnection().serverCommands().flushAll();
	}

	@Test
	@DisplayName("리프레시 토큰을 저장한다")
	void given_RefreshToken_when_save_then_return_savedRefreshToken() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(expiredAt)
			.memberId(memberId)
			.build();

		// when
		final RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

		// then
		assertThat(savedRefreshToken).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("저장된 리프레시 토큰의 유효기간은 ${token.refresh-expired-time}과 유효범위내로 같다")
	void given_savedRefreshToken_when_check_ExpiredTime_then_equals_refreshExpiredTime() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(expiredAt)
			.memberId(memberId)
			.build();
		refreshTokenRepository.save(refreshToken);

		// when
		final Long actual = redisTemplate.getExpire(refreshToken.refreshToken(), TimeUnit.MILLISECONDS);

		// then
		assertThat(actual).isCloseTo(expireLength, withinPercentage(5));
	}

	@Test
	@DisplayName("리프레시 토큰의 REDIS TTL 적용되었음을 확인한다.")
	void given_savedRefreshToken_when_save_savedRefreshToken_then_throw_UnAuthorizedRefreshTokenException() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(LocalDateTime.now())
			.memberId(memberId)
			.build();
		final RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

		// when & then
		assertThatThrownBy(() -> refreshTokenRepository.save(savedRefreshToken))
			.isInstanceOf(UnAuthorizedRefreshTokenException.class);
	}

	@Test
	@DisplayName("리프레시 토큰의 내용을 통해 조회한다")
	void given_save_RefreshToken_when_findRefreshToken_then_return_savedRefreshToken() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(LocalDateTime.now())
			.memberId(memberId)
			.build();

		refreshTokenRepository.save(refreshToken);

		// when
		final RefreshToken actual = refreshTokenRepository.findToken(tokenValue);

		// then
		assertThat(actual).isEqualTo(refreshToken);
	}

	@Test
	@DisplayName("존재하지 않는 리프레시 토큰을 조회하면 401 토큰 예외를 던진다")
	void given_when_findRefreshToken_then_throw_UnAuthorizedRefreshTokenException() {
		// when & then
		assertThatThrownBy(() -> refreshTokenRepository.findToken(tokenValue))
			.isInstanceOf(UnAuthorizedRefreshTokenException.class);
	}

	@Test
	@DisplayName("리프레시 토큰의 내용을 통해 리프레시 토큰을 삭제한다")
	void given_save_RefreshToken_when_deleteRefreshToken_then_findRefreshToken_throw_UnAuthorizedRefreshTokenException() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(LocalDateTime.now())
			.memberId(memberId)
			.build();
		refreshTokenRepository.save(refreshToken);

		// when
		refreshTokenRepository.delete(tokenValue);

		// then
		assertThatThrownBy(() -> refreshTokenRepository.findToken(tokenValue))
			.isInstanceOf(UnAuthorizedRefreshTokenException.class);
	}
}
