package com.gabia.bshop.dto.response;

public record LoginResponse(
	String token,
	boolean registerCompleted,
	LoginMemberResponse loginMemberResponse
) {
}
