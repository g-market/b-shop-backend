package com.gabia.bshop.security.redis;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;

@Builder
@RedisHash(value = "refresh_token")
public record RefreshToken(
	@Id String refreshToken,
	Long memberId,
	LocalDateTime expiredAt
) {

	public boolean isExpired() {
		return expiredAt.isBefore(LocalDateTime.now());
	}
}
