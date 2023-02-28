package com.gabia.bshop.dto.response;

public record LoginResult(
	String refreshToken,
	String accessToken,
	MemberResponse memberResponse
) {
}
