package com.gabia.bshop.security.provider;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.gabia.bshop.security.RefreshToken;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RefreshTokenProvider {

	private final TokenProperties tokenProperties;

	public RefreshToken createToken(final Long memberId) {
		final int days = (int)TimeUnit.MILLISECONDS.toDays(tokenProperties.getRefreshExpiredTime());
		final LocalDateTime expireDateTime = LocalDateTime.now().plusDays(days);
		return RefreshToken.builder()
			.refreshToken(UUID.randomUUID().toString())
			.memberId(memberId)
			.expiredAt(expireDateTime)
			.build();
	}
}
