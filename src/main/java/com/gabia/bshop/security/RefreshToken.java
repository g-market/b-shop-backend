package com.gabia.bshop.security;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record RefreshToken(
	String refreshToken,
	Long memberId,
	LocalDateTime expiredAt
) {

	public boolean isExpired() {
		return expiredAt.isBefore(LocalDateTime.now());
	}
}
