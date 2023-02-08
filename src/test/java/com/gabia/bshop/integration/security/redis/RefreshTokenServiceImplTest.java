package com.gabia.bshop.integration.security.redis;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gabia.bshop.exception.UnAuthorizedException;
import com.gabia.bshop.integration.IntegrationTest;
import com.gabia.bshop.security.redis.RefreshToken;
import com.gabia.bshop.security.redis.RefreshTokenRepository;
import com.gabia.bshop.security.redis.RefreshTokenService;

@SpringBootTest
class RefreshTokenServiceImplTest extends IntegrationTest {

	private final String tokenValue = "tokenValue";
	private final Long memberId = 1L;
	@Autowired
	RefreshTokenService refreshTokenService;
	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@AfterEach
	void tearDown() {
		refreshTokenRepository.deleteAll();
	}

	@Test
	void 리프레시_토큰을_저장한다() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(LocalDateTime.now())
			.memberId(memberId)
			.build();
		// when
		final RefreshToken savedRefreshToken = refreshTokenService.save(refreshToken);
		// then
		assertThat(savedRefreshToken).isEqualTo(refreshToken);
	}

	@Test
	void 리프레시_토큰의_내용을_통해_조회한다() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(LocalDateTime.now())
			.memberId(memberId)
			.build();
		refreshTokenService.save(refreshToken);
		// when
		final RefreshToken findRefreshToken = refreshTokenService.findToken(tokenValue);
		// then
		assertThat(findRefreshToken).isEqualTo(refreshToken);
	}

	@Test
	void 존재하지_않는_리프레시_토큰을_조회한다() {
		// when & then
		assertThatThrownBy(() -> refreshTokenService.findToken(tokenValue))
			.isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	void 리프레시_토큰의_내용을_통해_리프레시_토큰을_삭제한다() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(LocalDateTime.now())
			.memberId(memberId)
			.build();
		refreshTokenService.save(refreshToken);
		// when
		refreshTokenService.delete(tokenValue);
		// then
		assertThat(refreshTokenRepository.findById(tokenValue)).isEqualTo(Optional.empty());
	}

	@Test
	void 여러_개의_리프레시_토큰이_등록될_경우_예외가_발생한다() {
		// given
		final RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(tokenValue)
			.expiredAt(LocalDateTime.now())
			.memberId(memberId)
			.build();
		// when & then
		refreshTokenService.save(refreshToken);
		assertThatThrownBy(() -> refreshTokenService.save(refreshToken))
			.isInstanceOf(UnAuthorizedException.class);
	}
}
